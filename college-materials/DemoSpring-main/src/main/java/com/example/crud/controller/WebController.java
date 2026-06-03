package com.example.crud.controller;

import com.example.crud.model.Post;
import com.example.crud.model.Profile;
import com.example.crud.model.Tag;
import com.example.crud.model.User;
import com.example.crud.service.PostService;
import com.example.crud.service.ProfileService;
import com.example.crud.service.TagService;
import com.example.crud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final ProfileService profileService;
    private final PostService postService;
    private final TagService tagService;

    // DASHBOARD
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("totalPosts", postService.count());
        model.addAttribute("totalProfiles", profileService.findAll().size());
        model.addAttribute("totalTags", tagService.count());
        model.addAttribute("recentUsers", userService.findAll());
        model.addAttribute("currentPage", "dashboard");
        return "dashboard";
    }

    // USER - LIST
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageTitle", "Manajemen User");
        model.addAttribute("currentPage", "users");
        return "users/list";
    }

    @GetMapping("/users/create")
    public String userCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Tambah User");
        model.addAttribute("currentPage", "users");
        return "users/form";
    }

    @PostMapping("/users/create")
    public String userCreate(@Valid @ModelAttribute User user,
                              BindingResult result, Model model,
                              RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Tambah User");
            model.addAttribute("currentPage", "users");
            return "users/form";
        }
        try {
            userService.save(user);
            redirectAttrs.addFlashAttribute("successMsg", "User berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String userEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Edit User");
        model.addAttribute("currentPage", "users");
        return "users/form";
    }

    @PostMapping("/users/edit/{id}")
    public String userEdit(@PathVariable Long id, @Valid @ModelAttribute User user,
                            BindingResult result, Model model,
                            RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("currentPage", "users");
            return "users/form";
        }
        try {
            userService.update(id, user);
            redirectAttrs.addFlashAttribute("successMsg", "User berhasil diupdate!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/users/delete/{id}")
    public String userDelete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            userService.delete(id);
            redirectAttrs.addFlashAttribute("successMsg", "User berhasil dihapus!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/detail")
    public String userDetail(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));
        List<Post> posts = postService.findByUserId(id);
        Profile profile = profileService.findByUserId(id).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("profile", profile);
        model.addAttribute("currentPage", "users");
        return "users/detail";
    }

    // PROFILE
    @GetMapping("/profiles")
    public String profileList(Model model) {
        model.addAttribute("profiles", profileService.findAll());
        model.addAttribute("pageTitle", "Manajemen Profile");
        model.addAttribute("currentPage", "profiles");
        return "profiles/list";
    }

    @GetMapping("/profiles/create")
    public String profileCreateForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageTitle", "Tambah Profile");
        model.addAttribute("currentPage", "profiles");
        return "profiles/form";
    }

    @PostMapping("/profiles/create")
    public String profileCreate(@ModelAttribute Profile profile,
                                 @RequestParam Long userId,
                                 RedirectAttributes redirectAttrs) {
        try {
            profileService.save(profile, userId);
            redirectAttrs.addFlashAttribute("successMsg", "Profile berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/profiles";
    }

    @GetMapping("/profiles/edit/{id}")
    public String profileEditForm(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile tidak ditemukan!"));
        model.addAttribute("profile", profile);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageTitle", "Edit Profile");
        model.addAttribute("currentPage", "profiles");
        return "profiles/form";
    }

    @PostMapping("/profiles/edit/{id}")
    public String profileEdit(@PathVariable Long id, @ModelAttribute Profile profile,
                               RedirectAttributes redirectAttrs) {
        try {
            profileService.update(id, profile);
            redirectAttrs.addFlashAttribute("successMsg", "Profile berhasil diupdate!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/profiles";
    }

    @PostMapping("/profiles/delete/{id}")
    public String profileDelete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            profileService.delete(id);
            redirectAttrs.addFlashAttribute("successMsg", "Profile berhasil dihapus!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/profiles";
    }

    // POST
    @GetMapping("/posts")
    public String postList(Model model) {
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("pageTitle", "Manajemen Post");
        model.addAttribute("currentPage", "posts");
        return "posts/list";
    }

    @GetMapping("/posts/create")
    public String postCreateForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("statuses", Post.PostStatus.values());
        model.addAttribute("pageTitle", "Tambah Post");
        model.addAttribute("currentPage", "posts");
        return "posts/form";
    }

    @PostMapping("/posts/create")
    public String postCreate(@Valid @ModelAttribute Post post, BindingResult result,
                              @RequestParam Long userId,
                              @RequestParam(required = false) Set<Long> tagIds,
                              Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("statuses", Post.PostStatus.values());
            model.addAttribute("pageTitle", "Tambah Post");
            model.addAttribute("currentPage", "posts");
            return "posts/form";
        }
        try {
            Post saved = postService.save(post, userId);
            if (tagIds != null && !tagIds.isEmpty()) {
                tagService.setTagsForPost(saved.getId(), tagIds);
            }
            redirectAttrs.addFlashAttribute("successMsg", "Post berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/edit/{id}")
    public String postEditForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan!"));
        model.addAttribute("post", post);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("statuses", Post.PostStatus.values());
        model.addAttribute("pageTitle", "Edit Post");
        model.addAttribute("currentPage", "posts");
        return "posts/form";
    }

    @PostMapping("/posts/edit/{id}")
    public String postEdit(@PathVariable Long id, @Valid @ModelAttribute Post post,
                            BindingResult result,
                            @RequestParam(required = false) Set<Long> tagIds,
                            Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("statuses", Post.PostStatus.values());
            model.addAttribute("pageTitle", "Edit Post");
            model.addAttribute("currentPage", "posts");
            return "posts/form";
        }
        try {
            postService.update(id, post);
            tagService.setTagsForPost(id, tagIds != null ? tagIds : new java.util.HashSet<>());
            redirectAttrs.addFlashAttribute("successMsg", "Post berhasil diupdate!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/posts";
    }

    @PostMapping("/posts/delete/{id}")
    public String postDelete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            postService.delete(id);
            redirectAttrs.addFlashAttribute("successMsg", "Post berhasil dihapus!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/posts";
    }

    // =============================================
    // TAG - CRUD
    // =============================================
    @GetMapping("/tags")
    public String tagList(Model model) {
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("postCountMap", tagService.getPostCountPerTag());
        model.addAttribute("pageTitle", "Manajemen Tag");
        model.addAttribute("currentPage", "tags");
        return "tags/list";
    }

    @GetMapping("/tags/create")
    public String tagCreateForm(Model model) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("pageTitle", "Tambah Tag");
        model.addAttribute("currentPage", "tags");
        return "tags/form";
    }

    @PostMapping("/tags/create")
    public String tagCreate(@Valid @ModelAttribute Tag tag, BindingResult result,
                             Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Tambah Tag");
            model.addAttribute("currentPage", "tags");
            return "tags/form";
        }
        try {
            tagService.save(tag);
            redirectAttrs.addFlashAttribute("successMsg", "Tag berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/tags";
    }

    @GetMapping("/tags/edit/{id}")
    public String tagEditForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag tidak ditemukan!"));
        model.addAttribute("tag", tag);
        model.addAttribute("pageTitle", "Edit Tag");
        model.addAttribute("currentPage", "tags");
        return "tags/form";
    }

    @PostMapping("/tags/edit/{id}")
    public String tagEdit(@PathVariable Long id, @Valid @ModelAttribute Tag tag,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Tag");
            model.addAttribute("currentPage", "tags");
            return "tags/form";
        }
        try {
            tagService.update(id, tag);
            redirectAttrs.addFlashAttribute("successMsg", "Tag berhasil diupdate!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/tags";
    }

    @PostMapping("/tags/delete/{id}")
    public String tagDelete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            tagService.delete(id);
            redirectAttrs.addFlashAttribute("successMsg", "Tag berhasil dihapus!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/tags";
    }
}
