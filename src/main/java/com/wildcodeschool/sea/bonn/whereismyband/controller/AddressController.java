package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;

@Controller
@RequestMapping("/address")
public class AddressController {

	private AddressRepository addressRepository;

	@Autowired
	public AddressController(AddressRepository addressRepository) {
		super();
		this.addressRepository = addressRepository;
	}

	@GetMapping("list")
	public String getAll(Model model) {

		model.addAttribute("addresses", addressRepository.findAll());

		return "addresses";
	}

	@GetMapping("edit")
	public String getAddress(Model model,
			@RequestParam(required = false) Long id) {

		// Create an empty Address object
		Address address = new Address();

		// if an id was sent as a parameter
		if (id != null) {
			//retrieve object from database
			Optional<Address> optionalAddress = addressRepository.findById(id);
			// if database object could be retrieved
			if (optionalAddress.isPresent()) {
				// set address to the object retrieved
				address = optionalAddress.get();
			}
		}

		// add address to the view model (either empty or prefilled with DB data)
		model.addAttribute("address", address);

		return "address";
	}

	@PostMapping("edit")
	public String postAddress(@ModelAttribute Address address) {

		addressRepository.save(address);

		return "redirect:list";
	}

	@GetMapping("delete")
	public String deleteAddress(@RequestParam Long id) {

		addressRepository.deleteById(id);

		return "redirect:list";
	}



}


