package com.alhas.airbnb.listing.presentation;

import com.alhas.airbnb.listing.TenantService;
import com.alhas.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayListingDTO;
import com.alhas.airbnb.listing.domain.BookingCategory;
import com.alhas.airbnb.sharedkernel.service.State;
import com.alhas.airbnb.sharedkernel.service.StatusNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-listing")
public class TenantResource {

    private final TenantService tenantService;

    public TenantResource(TenantService tenantService) {
        this.tenantService = tenantService;
    }


    @GetMapping("/get-all-by-category")
    public ResponseEntity<Page<DisplayCardListingDTO>> findAllByBookingCategory(Pageable pageable,
                                                                                @RequestParam BookingCategory category) {
        //System.out.println("pageable /get-all-by-category: " + pageable);
        return ResponseEntity.ok(tenantService.getAllByCategory(pageable, category));
    }

    @GetMapping("/get-one")
    public ResponseEntity<DisplayListingDTO> getOne(@RequestParam UUID publicId) {
        State<DisplayListingDTO, String> displayListingState = tenantService.getOne(publicId);
        if(displayListingState.getStatus().equals(StatusNotification.OK)){
            return ResponseEntity.ok(displayListingState.getValue());
        }

       else{
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, displayListingState.getError());
            return ResponseEntity.of(problemDetail).build();
        }



    }


}


