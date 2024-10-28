package vn.iotstar.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.services.ICategoryService;

public class CategoryController {
	@Autowired
	private ICategoryService categoryService;

	@GetMapping("add")
	public String add(ModelMap model) {
		Category category = new Category();
		model.addAttribute("category", category);
		return "admin/categories/addOrEdit";
	}

	@PostMapping("save")
	public String save(@Valid @ModelAttribute("category") Category category, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "admin/categories/addOrEdit";
		}

		categoryService.save(category);
		model.addAttribute("message", "Category is saved successfully!");
		return "redirect:/admin/categories";
	}

	@GetMapping("")
	public String list(ModelMap model) {
		List<Category> list = categoryService.findAll();
		model.addAttribute("categories", list);
		return "admin/categories/list";
	}

	@GetMapping("edit/{categoryId}")
	public String edit(@PathVariable("categoryId") Long categoryId, ModelMap model) {
		Optional<Category> category = categoryService.findById(categoryId);
		if (category.isPresent()) {
			model.addAttribute("category", category.get());
			return "admin/categories/addOrEdit";
		} else {
			model.addAttribute("message", "Category not found!");
			return "redirect:/admin/categories";
		}
	}

	@GetMapping("delete/{categoryId}")
	public String delete(@PathVariable("categoryId") Long categoryId, ModelMap model) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category deleted successfully!");
		return "redirect:/admin/categories";
	}

	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
		List<Category> list = (name != null && !name.isEmpty()) ? categoryService.findByNameContaining(name)
				: categoryService.findAll();
		model.addAttribute("categories", list);
		return "admin/categories/search";
	}

	@GetMapping("searchpaginated")
	public String searchPaginated(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);
		PageRequest pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));

		Page<Category> resultPage = (name != null && !name.isEmpty())
				? categoryService.findByNameContaining(name, pageable)
				: categoryService.findAll(pageable);

		model.addAttribute("categoryPage", resultPage);

		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);

			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 5;
				else if (start == 1)
					end = start + 5;
			}

			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("name", name);
		return "admin/categories/searchpaginated";

	}
}
