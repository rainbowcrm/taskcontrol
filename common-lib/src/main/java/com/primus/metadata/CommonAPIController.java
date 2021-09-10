package com.primus.metadata;

import com.primus.generic.BusinessContext;
import com.primus.generic.BusinessModel;
import com.primus.generic.GenericService;
import com.primus.metadata.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "https://localhost:20220", maxAge = 3600)
@RequestMapping("/api/commonui")
@RestController
public class CommonAPIController {

    @Autowired
    MetadataService metadataService ;

    @Autowired
    GenericService genericService ;

    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public ResponseEntity<Map> getListPageMeta(@RequestParam String pageid, @RequestParam Optional<Integer> from , @RequestParam Optional<Integer> to )
    {
        BusinessContext context = BusinessContext.createContext(SecurityContextHolder.getContext());
        Map ret = metadataService.getPage(pageid,from.orElse(null),to.orElse(null),null,context);
        ResponseEntity entity =  new ResponseEntity<Map>(ret, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/getListContent", method = RequestMethod.POST)
    public ResponseEntity<Map> getListPageContent(@RequestParam String pageid,
                                                  @RequestParam Optional<Integer> from ,
                                                  @RequestParam Optional<Integer> to,
                                                  @RequestBody Optional<Map> filter)
    {

        BusinessContext context = BusinessContext.createContext(SecurityContextHolder.getContext());
        Map ret = metadataService.getPage(pageid,from.orElse(null),to.orElse(null),filter.orElse(null),context);
        ResponseEntity entity =  new ResponseEntity<Map>(ret, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/getDataFromPK", method = RequestMethod.GET)
    public ResponseEntity<Map> getDataFromPK(@RequestParam String entity,  @RequestParam String pk)
    {
        BusinessContext context = BusinessContext.createContext(SecurityContextHolder.getContext());
        BusinessModel model = genericService.fetchData(entity,pk,context);
        Map ret = model.toMap(context) ;
        ResponseEntity responseEntity =  new ResponseEntity<Map>(ret, HttpStatus.OK);
        return  responseEntity;


    }



}
