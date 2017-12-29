//package eu.matrus.passmanager.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//@EnableResourceServer
//@EnableWebSecurity(debug = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
////    @Value("${security.oauth2.resource.id}")
////    private String resourceId;
////
////    @Override
////    public void configure(ResourceServerSecurityConfigurer resources) {
////        resources
////                .resourceId(resourceId);
////    }
////
////    @Override
////    public void configure(HttpSecurity http) throws Exception {
////        http
////                .anonymous().disable()
////                .csrf().disable()
////                .authorizeRequests()
////                .antMatchers(HttpMethod.POST, "/users").permitAll()
////                .antMatchers("/users/**").authenticated()
////                .antMatchers("passwords/**").authenticated().and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////
////    }
//
//    private static class OAuthRequestedMatcher implements RequestMatcher {
//        public boolean matches(HttpServletRequest request) {
//            String auth = request.getHeader("Authorization");
//            // Determine if the client request contained an OAuth Authorization
//            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
//            boolean haveAccessToken = request.getParameter("access_token") != null;
//            return haveOauth2Token || haveAccessToken;
//        }
//    }
//    private static class BasicRequestMatcher implements RequestMatcher {
//        @Override
//        public boolean matches(HttpServletRequest request) {
//            String auth = request.getHeader("Authorization");
//            return (auth != null && auth.startsWith("Basic"));
//        }
//    }
//
//}