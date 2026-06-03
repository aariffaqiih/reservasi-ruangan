package com.tup.reservasi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - Konfigurasi ini menjaga starter login dan isolasi akses role.
 * - Role mengikuti turunan User pada class-diagram:
 *   Mahasiswa, Admin, dan Satpam.
 * - Jika controller fitur final ditambahkan, pastikan URL tetap hanya bisa
 *   diakses oleh role yang sesuai jobdesk.
 */
@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/api/**"))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
						.requestMatchers("/api/users/register").permitAll()
						.requestMatchers("/api/approvals/**").hasRole("ADMIN")
						.requestMatchers("/api/rooms/**").hasRole("ADMIN")
						.requestMatchers("/api/reservations/**").hasAnyRole("MAHASISWA", "ADMIN")
						.requestMatchers("/api/users/**").hasAnyRole("MAHASISWA", "ADMIN")
						.requestMatchers("/mahasiswa/**").hasRole("MAHASISWA")
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/satpam/**").hasRole("SATPAM")
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/dashboard", true)
						.permitAll())
				.logout(logout -> logout
						.logoutSuccessUrl("/login?logout")
						.permitAll())
				.sessionManagement(session -> session
						.sessionFixation(fixation -> fixation.migrateSession()));

		return http.build();
	}
}
