package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import demo.model.CreditCardBill;
import demo.service.CreditCardBillService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/teste")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditCardBillController {
	
	private final CreditCardBillService creditCardBillService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("creditCardBill", new CreditCardBill());
		return "creditCardBill";
	}
	
	@RequestMapping(value = "/executar", method = RequestMethod.POST)
	public String teste(Model model) {
		CreditCardBill creditCardBill = creditCardBillService.teste();
		model.addAttribute("creditCardBill", creditCardBill);
		return "creditCardBill";
	}

}
