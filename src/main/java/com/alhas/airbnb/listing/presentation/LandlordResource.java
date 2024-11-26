package com.alhas.airbnb.listing.presentation;

import com.alhas.airbnb.listing.application.dto.CreatedListingDTO;
import com.alhas.airbnb.listing.application.dto.LandlordService;
import com.alhas.airbnb.listing.application.dto.SaveListingDTO;
import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.user.application.dto.UserException;
import com.alhas.airbnb.user.application.dto.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/landlord-listing")
public class LandlordResource {
    private final LandlordService landlordService;
    private final Validator validator;
    private final UserService userService;

    private ObjectMapper objectMapper=new ObjectMapper();

    public LandlordResource(LandlordService landlordService, UserService userService, LandlordService landlordService1, Validator validator, UserService userService1) {
        this.landlordService = landlordService1;
        this.validator = validator;
        this.userService = userService1;
    }

    private static PictureDTO apply(MultipartFile file) {
        try {
// Convertir chaque MultipartFile en PictureDTO
            byte[] fileBytes = file.getBytes(); // Assume only one file per key
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
        System.out.println("saveListingDTOString dans landlord Ressource  :" + saveListingDTOString);

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


    private static Function<MultipartFile, PictureDTO> mapMultipartFilePictureDTO(){
        return multifileFile-> {
            try {
                return  new PictureDTO(multifileFile.getBytes(),multifileFile.getContentType(),false);
                        
            }catch(IOException ioe){
             throw new UserException(String.format("Can't not parse multipart : %s ",multifileFile.getOriginalFilename()));
            }
        };
    }
}
