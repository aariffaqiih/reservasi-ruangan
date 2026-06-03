package com.tup.reservasi;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginSecurityTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void mahasiswaCanLoginAndCannotOpenOtherRolePages() throws Exception {
		MockHttpSession session = login("mhs", "mhs", "/mahasiswa/dashboard");

		mockMvc.perform(get("/mahasiswa/dashboard").session(session))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("ini halaman mahasiswa")));

		mockMvc.perform(get("/admin/dashboard").session(session))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/satpam/dashboard").session(session))
				.andExpect(status().isForbidden());
	}

	@Test
	void adminCanLoginAndCannotOpenOtherRolePages() throws Exception {
		MockHttpSession session = login("adm", "adm", "/admin/dashboard");

		mockMvc.perform(get("/admin/dashboard").session(session))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("ini halaman admin")));

		mockMvc.perform(get("/mahasiswa/dashboard").session(session))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/satpam/dashboard").session(session))
				.andExpect(status().isForbidden());
	}

	@Test
	void satpamCanLoginAndCannotOpenOtherRolePages() throws Exception {
		MockHttpSession session = login("stm", "stm", "/satpam/dashboard");

		mockMvc.perform(get("/satpam/dashboard").session(session))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("ini halaman satpam")));

		mockMvc.perform(get("/mahasiswa/dashboard").session(session))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/admin/dashboard").session(session))
				.andExpect(status().isForbidden());
	}

	private MockHttpSession login(String username, String password, String dashboardUrl) throws Exception {
		MvcResult loginResult = mockMvc.perform(formLogin("/login")
						.user(username)
						.password(password))
				.andExpect(authenticated().withUsername(username))
				.andExpect(redirectedUrl("/dashboard"))
				.andReturn();

		MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

		mockMvc.perform(get("/dashboard").session(session))
				.andExpect(redirectedUrl(dashboardUrl));

		return session;
	}
}
