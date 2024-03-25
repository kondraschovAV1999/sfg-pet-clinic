package learning.spring.controllers;

import learning.spring.model.Owner;
import learning.spring.services.OwnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/owners")
@Controller
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping({"/find"})
    public String findOwners(@ModelAttribute("owner") Owner owner) {
        return "owners/findOwners";
    }

    @GetMapping("")
    public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
                                  Model model) {
        // allow parameterless GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
        if (ownersResults.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        }

        if (ownersResults.getTotalElements() == 1) {
            // 1 owner found
            owner = ownersResults.iterator().next();
            return "redirect:/owners/" + owner.getId();
        }

        // multiple owners found
        return addPaginationModel(page, model, ownersResults);
    }

    private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return ownerService.findByLastNameContainingIgnoreCase(lastname, pageable);
    }

    private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
        List<Owner> listOwners = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listOwners", listOwners);
        return "owners/ownersList";
    }

    @GetMapping("{ownerId}")
    public String showOwner(@PathVariable Long ownerId, Model model) {

        model.addAttribute("owner", ownerService.findById(ownerId));
        return "owners/ownerDetails";
    }

}
