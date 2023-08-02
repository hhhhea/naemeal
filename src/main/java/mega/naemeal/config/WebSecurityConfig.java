package mega.naemeal.config;

import lombok.RequiredArgsConstructor;
import mega.naemeal.jwt.JwtAuthFilter;
import mega.naemeal.jwt.JwtUtil;
import mega.naemeal.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {

  private final JwtUtil jwtUtil;

  private final UserDetailsServiceImpl userDetailsService;

  public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

  private final CorsConfig corsConfig;

  @Autowired
  public WebSecurityConfig(JwtUtil jwtUtil, CorsConfig corsConfig,
      UserDetailsServiceImpl userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.corsConfig = corsConfig;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf().disable()
//        .cors();
//        http.authorizeRequests()
//        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//        .requestMatchers("/users/signup").permitAll()
//        .requestMatchers("/users/signin").permitAll()
//        .requestMatchers("/admin/**").hasRole("ADMIN")
//        .requestMatchers("/cookProgram/**").permitAll()
//        .requestMatchers("/notices/**").permitAll()
//        .requestMatchers("/profileImage/**").hasAnyRole("USER",  "ADMIN")
//        .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
//        .anyRequest().authenticated()
//        .and().addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService),
//            UsernamePasswordAuthenticationFilter.class);
//
//    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    http.formLogin().disable();
//
//    return http.build();
//
//  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // CSRF 설정
    http.csrf(AbstractHttpConfigurer::disable);

    // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
    http.sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
//            .requestMatchers("/","/users/signup","/users/signin","/cookProgram/**","/notices/**").permitAll()
                .requestMatchers("/users/signup").permitAll()
                .requestMatchers("/users/signin").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/cookProgram/**").permitAll()
                .requestMatchers("/notices/**").permitAll()
                .requestMatchers("/profileImage/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
    );

    http.formLogin(AbstractHttpConfigurer::disable);

    http.addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(corsConfig.corsFilter(), JwtAuthFilter.class);

    return http.build();
  }

//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")
//        .exposedHeaders("Authorization")
//        .allowedOrigins("*") // 허용할 출처
//        .allowCredentials(true) // 쿠키 인증 요청 허용
//        .allowedMethods("*")
//        .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
//  }
//
//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration config = new CorsConfiguration();
////        config.addAllowedOriginPattern("*");
//    config.setAllowCredentials(true);
//    config.addAllowedOrigin("*");
//    config.addAllowedHeader("*");
//    config.addAllowedMethod("*");
//    config.addExposedHeader("*"); // https://iyk2h.tistory.com/184?category=875351 // 헤더값 보내줄 거 설정.
//
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", config);
//    return source;
//  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowCredentials(true)
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
        .allowedOriginPatterns("*")
        .allowedHeaders("*")
        .exposedHeaders("Authorization", "RefreshToken");
  }

}