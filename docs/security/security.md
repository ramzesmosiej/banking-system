# Security

### User authentication & authorization

Komponent tworzony zgodnie z dokumentacją [Spring Security](https://docs.spring.io/spring-security/reference/index.html)

#### Spis treści 

 - [Authentication](#authentication)
   - [Login i Pozyskiwanie tokenu JWT](#login-i-pozyskiwanie tokenu-JWT)
   - [Mechanizm Autentykacji w Spring Security](#diagram)
   - [Custom Authentication Provider](#custom-authentication-provider)
 - [Authorization](#authorization)
   - [Weryfikacja Tokenem JWT](#weryfikacja tokenem JWT)
   - [Weryfikowanie Ról](#weryfikowanie ról)

#### Authentication

Podstawowa autentykacja (ustalenie tożsamości użytkownika) w serwisie odbywa się przy pomocy podania daych logowania. W tym celu stworzony jest odpowiedni endpoint
`/api/auth/login`, który przyjmuje `login` i `password` i zwraca podpisany token JWT zawierający `username` danego użytkownika. Token ten musi być potem użyty, aby uzyskać dostęp to chronionych zasobów.

#### Login i Pozyskiwanie tokenu JWT
Endpoint `/api/auth/login` znajduje się w kontrolerze `rest/securityController.java`.

`````
   @PostMapping("/api/auth/login")
   public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
      authenticationManager.authenticate(token);
      // if there is no exception thrown from authentication manager,
      // we can generate a JWT token and give it to user.
      String jwtToken = jwtUtil.generateAccessToken(loginRequest.getUsername());
      return ResponseEntity.ok(jswToken);
   }
`````

Generowaniem tokenów zajmuje się klasa JwtUtil. Proces autentykacji zaczyna się poprzez wywołanie metody authenticate na klasie `AuthenticationManager`,
która tworzy obiekt `Authentication`. Implementacja tej metody znajduje się w klasie `ProviderManager` która zawiera wszystkie skonfigurowane przez nas `AuthenticationProvider`. Defaultowa implementacja to `DAOAuthenticationProvider`, 
który szuka usera po username w bazie, a następnie porównuje wpisane hasło z hasłem w bazie danych.

#### Diagram
Poniższy diagram przedstawia mechanizm autentykacji w Spring Security:

![Authorization Architecture](C:\Users\RMo\IdeaProjects\banking-system\docs\security\mechanism.PNG "Authorization architecture")

#### Custom authentication provider

W naszym przypadku potrzebny jest własny `BankAuthenticationProvider` `security\BankAuthenticationProvider` nadpisującego metodę `authenticate`, 
który nie tylko będzie sprawdzał hasło,
ale też upewniał się że konto zostało aktywowane.
``````
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        AppUser user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in database"));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority -> {
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        });
        if (encoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }
        throw new BadCredentialsException("error");
    }
``````
Pozwala to na rozwijanie procesu autentykacji w przyszłości. Klasę
tą należy dołączyć do AuthenticationManagera w następujący sposób:
`````
    // bankAuthenticationProvider is injected before using constructor dependency injection
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.eraseCredentials(false)
                .authenticationProvider(bankAuthenticationProvider);
        return builder.build();
    }
`````

#### Authorization

#### Weryfikacja tokenem JWT


Dodanie `.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)` sprawia że
że nie będzie tworzona żadna sesja z użyciem Spring Security – autentykacja odbywać się będzie poprzez wywołanie filtra weryfikującego każde żądanie.
Każdy chroniony request jest weryfikowany tokenem JWT. W tym celu w klasie `SecurityConfig` należy
dodać nasz własny filtr sprawdzający czy token jest ważny i prawidłowy.
``````
   http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
``````
``````
@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    // this method will be called every time during filter chain 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            // if header not exists skip this filter and continue
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1].trim();
        if (!jwtUtil.validate(token)) {
            // token will be invalid meaning authentication will be not successful
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUserName(token);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null,  new ArrayList<>());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // give the authentication token with authenticated=true to security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
``````

W powyższy sposób dajemy znać dla kontekstu springa że użytkownik został już uwierzytelniony na czas 
określony w konfiguracji tworzenia tokenu (u nas 30 minut)

#### Weryfikowanie ról

// t.b.d







