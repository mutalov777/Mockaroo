package uz.mockaroo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.mockaroo.entity.Data;
import uz.mockaroo.enums.FakerType;
import uz.mockaroo.enums.FileFormat;
import uz.mockaroo.service.DataService;


@Controller
@RequestMapping( "/" )
public record DataController(DataService service) {

    @GetMapping( "/data" )
    public ResponseEntity<?> get( @RequestBody Data data ) {
        return service.generate( data );
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView getFakerType( Model model) {
        model.addAttribute("fakerTypes", FakerType.values() );
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/getFormat", method = RequestMethod.GET)
    public ModelAndView getFormat( Model model) {
        model.addAttribute( "formats" , FileFormat.values() );

        return new ModelAndView( "index" );

    }

}
