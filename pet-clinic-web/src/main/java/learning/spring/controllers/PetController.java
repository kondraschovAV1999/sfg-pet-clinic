package learning.spring.controllers;

import jakarta.validation.Valid;
import learning.spring.model.Owner;
import learning.spring.model.Pet;
import learning.spring.model.PetType;
import learning.spring.services.OwnerService;
import learning.spring.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collection;


@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private final OwnerService ownerService;
    private final PetTypeService petTypeService;
    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    public PetController(OwnerService ownerService, PetTypeService petTypeService) {
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
    }


    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

//    @InitBinder("pet")
//    public void initPetBinder(WebDataBinder dataBinder) {
//        dataBinder.setValidator(new PetValidator());
//    }


    @GetMapping("/pets/new")
    public String initCreationForm(@ModelAttribute Owner owner, @ModelAttribute Pet pet) {
        owner.addPet(pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result,
                                      Model model, RedirectAttributes redirectAttributes) {
        if (StringUtils.hasText(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }

        LocalDate currentDate = LocalDate.now();
        if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
            result.rejectValue("birthDate", "typeMismatch.birthDate");
        }

        owner.addPet(pet);
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        }

        var savedOwner = ownerService.save(owner);
        redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
        return "redirect:/owners/" + savedOwner.getId();
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@ModelAttribute Owner owner, @PathVariable("petId") Long petId,
                                 Model model) {
        Pet pet = owner.getPet(petId);
        model.addAttribute("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, @PathVariable Long petId,
                                    BindingResult result, Owner owner, Model model,
                                    RedirectAttributes redirectAttributes) {
        owner.removePet(owner.getPet(petId));//removing an old pet
        String petName = pet.getName();

        // checking if the pet name already exist for the owner
        if (StringUtils.hasText(petName)) {
            Pet existingPet = owner.getPet(petName.toLowerCase(), false);
            if (existingPet != null && existingPet.getId().equals(pet.getId())) {
                result.rejectValue("name", "duplicate", "already exists");
            }
        }

        LocalDate currentDate = LocalDate.now();
        if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
            result.rejectValue("birthDate", "typeMismatch.birthDate");
        }

        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        }

        owner.addPet(pet);
        var savedOwner = ownerService.save(owner);
        redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
        return "redirect:/owners/" + savedOwner.getId();
    }

}
