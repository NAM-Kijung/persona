package com.ssafy.project.common.security.filter;

import com.ssafy.project.common.provider.TokenProvider;
import com.ssafy.project.common.security.exception.CustomAuthException;
import com.ssafy.project.common.security.service.CustomUserDetailsService;
import com.ssafy.project.common.util.constant.CommonErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            log.info("무조건오는토큰 : {}", String.valueOf(tokenProvider.getTokenFromRequest(request)));
            log.info(tokenProvider.getTokenFromRequest(request).equals("Bearer "));

        try {
            String token = tokenProvider.getTokenFromRequest(request);

            if (StringUtils.hasText(token) && token.length()> 10 && tokenProvider.validateToken(token)) {
                log.info("토큰 값 {}", token);
                Long id = tokenProvider.getUserIdFromToken(token);

                    UserDetails userDetails = customUserDetailsService.loadUserById(id);

                    // Object 타입의 principal의 식별자에 더해 상세정보를 담기 위해 자리에 userDetails를 set
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // Object 타입의 details에 request에있는 사용자가 인증한 웹 요청과 관련된 세부 정보를 set
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.info("에러 쓰로우");

        }

        filterChain.doFilter(request, response);
    }

}