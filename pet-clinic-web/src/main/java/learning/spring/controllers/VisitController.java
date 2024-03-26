package learning.spring.controllers;

import jakarta.validation.Valid;
import learning.spring.model.Owner;
import learning.spring.model.Pet;
import learning.spring.model.Visit;
import learning.spring.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequestMapping("/owners/{ownerId}/pets/{petId}")
@Controller
public class VisitController {
    private final OwnerService ownerService;

    public VisitController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
     * we always have fresh data - Since we do not use the session scope, make sure that
     * Pet object always has an id (Even though id is not part of the form fields)
     *
     * @param petId
     * @return Pet
     */
    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId,
                                  Model model) {
        Owner owner = ownerService.findById(ownerId);

        Pet pet = owner.getPet(petId);
        model.addAttribute("pet", pet);
        model.addAttribute("owner", owner);

        Visit visit = new Visit();
        pet.addVisit(visit);
        return visit;
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
    // called
    @GetMapping("visits/new")
    public String initNewVisitForm() {
        return "pets/createOrUpdateVisitForm";
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
    // called
    @PostMapping("visits/new")
    public String processNewVisitForm(@ModelAttribute Owner owner, @PathVariable Long petId, @Valid Visit visit,
                                      BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        }

        owner.addVisit(petId, visit);
        var savedOwner = ownerService.save(owner);
        redirectAttributes.addFlashAttribute("message", "Your visit has been booked");
        return "redirect:/owners/" + savedOwner.getId();
    }

}
