package com.alhas.airbnb.listing.presentation;

import com.alhas.airbnb.infrastructure.SecurityUtils;
import com.alhas.airbnb.listing.application.dto.CreatedListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.alhas.airbnb.listing.application.dto.LandlordService;
import com.alhas.airbnb.listing.application.dto.SaveListingDTO;
import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.sharedkernel.service.State;
import com.alhas.airbnb.sharedkernel.service.StatusNotification;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.application.dto.UserException;
import com.alhas.airbnb.user.application.dto.UserService;
import com.alhas.airbnb.user.domain.Authority;
import com.alhas.airbnb.user.domain.User;
import com.alhas.airbnb.user.mapper.UserMapper;
import com.alhas.airbnb.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/landlord-listing")
public class LandlordResource {
    private final LandlordService landlordService;
    private final Validator validator;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private ObjectMapper objectMapper=new ObjectMapper();

    public LandlordResource(LandlordService landlordService, UserService userService, LandlordService landlordService1, Validator validator, UserService userService1, UserRepository userRepository, UserMapper userMapper) {
        this.landlordService = landlordService1;
        this.validator = validator;
        this.userService = userService1;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private static PictureDTO apply(MultipartFile file) {
        try {
// Convertir chaque MultipartFile en PictureDTO
            byte[] fileBytes = file.getBytes(); // Assume only one file per key
            System.out.println(" &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(" byte[] fileBytes = file.getBytes().length"+ fileBytes.length);
            return new PictureDTO(fileBytes, file.getContentType(), false); // Vous pouvez ajuster 'isCover' si nécessaire
        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }
    }


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatedListingDTO> create(MultipartHttpServletRequest multipartHttpServletRequest,
                                                    @RequestPart(name = "dto") String saveListingDTOString) throws IOException {
        // Récupérer tous les fichiers dans la requête
        List<PictureDTO> pictures = multipartHttpServletRequest.getFileMap()
                .values()
                .stream()
                .map(mapMultipartFilePictureDTO())
                .collect(Collectors.toList());

        // Afficher le contenu de "dto" pour le débogage
        System.out.println("----------------/crate listing from front : ---------------------------------------");
        System.out.println(" ");
        System.out.println("saveListingDTOString dans landlord Ressource  :" + saveListingDTOString);
        System.out.println("MultipartHttpServletRequest multipartHttpServletRequest  :" + multipartHttpServletRequest);
        System.out.println("MultipartHttpServletRequest multipartHttpServletRequest.stream.map.toList  :" + pictures);
        System.out.println(" ");

        // Désérialiser le DTO
        SaveListingDTO saveListingDTO = objectMapper.readValue(saveListingDTOString, SaveListingDTO.class);

        // Valider les données
        Set<ConstraintViolation<SaveListingDTO>> violations = validator.validate(saveListingDTO);
        if (!violations.isEmpty()) {
            // Si il y a des violations, retourner une erreur de validation
            String violationsJoins = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationsJoins);
            return ResponseEntity.of(problemDetail).build();
        } else {
            // Si tout va bien, procéder à la création
            // Vous pouvez maintenant passer les pictures à votre service
            saveListingDTO.setPictures(pictures); // Assurez-vous que SaveListingDTO a un champ pour stocker les pictures
            return ResponseEntity.ok(landlordService.createdListingDTO(saveListingDTO));
        }
    }




    private static Function<MultipartFile, PictureDTO> mapMultipartFilePictureDTO(){
        return multifileFile-> {
            try {
                return  new PictureDTO(multifileFile.getBytes(),multifileFile.getContentType(),false);

            }catch(IOException ioe){
             throw new UserException(String.format("Can't not parse multipart : %s ",multifileFile.getOriginalFilename()));
            }
        };
    }

///landlord-listing/get-all
    @GetMapping(value = "/get-all")
    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<List<DisplayCardListingDTO>> getAll(){
        ReadUserDTO connectUser= userService.getAuthenticatedUserFromSecurityContext();
        User findOneByEmail= userRepository.findOneByEmail(connectUser.getEmail()).get();
        //findOneByEmail.setAuthorities();

        System.out.println(" findByOneEmail  :" + findOneByEmail);
       // Hibernate.initialize(findOneByEmail.getAuthorities());
        ReadUserDTO userDTO =userMapper.readUserDTOToUser(findOneByEmail) ;
//        userDTO.setPublicId(findOneByEmail.getPublicId());
//        Set<String> authorities= findOneByEmail.getAuthorities()
//                        .stream()
//                                .map(authority ->   String.valueOf(authority))
//                                        .collect(Collectors.toSet());
        //userDTO.setAuthorities(authorities);
        //DisplayCardListingDTO ={PictureDT0, publicId, category}
        //PictureDTO={byte [] file, filecontentType,isCover}
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(userDTO);

        System.out.println("----------------/get-all ---------------------------------------");
        System.out.println("  Set<String> authorities  = "+userDTO);
        System.out.println("this.http.get<Array<CardListing>>(`${environment.API_URL}/landlord-listing/get-all`)  :" + allProperties);
        System.out.println(" ");
        System.out.println("--------------------------allProperties.get(0).cover().file()  ==  "+allProperties.get(0).cover().file());
        System.out.println("------------------------------------------------------");
        return ResponseEntity.ok(allProperties);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<UUID> delete(@RequestParam UUID publicId) {
        ReadUserDTO connectUser= userService.getAuthenticatedUserFromSecurityContext();
        State<UUID, String> deleteState = landlordService.delete(publicId, connectUser);

        if(deleteState.getStatus().equals(StatusNotification.OK)){
            return ResponseEntity.ok(deleteState.getValue());
        } 
        else if (deleteState.getStatus().equals(StatusNotification.UNAUTHORIZED)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }









    /*
    public ResponseEntity<CreatedListingDTO> create(MultipartHttpServletRequest multipartHttpServletRequest,
                                                    @RequestPart(name = "dto") String saveListingDTOString) throws IOException {
        multipartHttpServletRequest.getFileMap()
                .values()
                .stream()
                .map(mapMultipartFilePictureDTO())
                .toList();
       System.out.println("saveListingDTOString dans landlord Ressource  :"+ saveListingDTOString);
        SaveListingDTO saveListingDTO =objectMapper.readValue(saveListingDTOString, SaveListingDTO.class);
        Set<ConstraintViolation<SaveListingDTO>> violations = validator.validate(saveListingDTO);
        if(!violations.isEmpty()) {
            String violationsJoins = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationsJoins);
            return  ResponseEntity.of(problemDetail).build();
        }
        else {
            return ResponseEntity.ok(landlordService.createdListingDTO(saveListingDTO));
        }


    }*/
}
