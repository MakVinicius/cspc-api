package uol.compass.cspcapi.application.api.scrumMaster;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;

import java.util.List;

@RestController
@RequestMapping("/scrummasters")
public class ScrumMasterController {

    private ScrumMasterService scrumMasterService;

    @Autowired
    public ScrumMasterController(ScrumMasterService scrumMasterService) {
        this.scrumMasterService = scrumMasterService;
    }

    @PostMapping
    public ResponseEntity<ResponseScrumMasterDTO> save(@Valid @RequestBody CreateScrumMasterDTO scrumMaster)
    {
        return new ResponseEntity<>(
                scrumMasterService.save(scrumMaster),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseScrumMasterDTO> getById(@PathVariable Long id){
        return new ResponseEntity<>(
                scrumMasterService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseScrumMasterDTO>> getAllScrumMaster(){
        return new ResponseEntity<>(
                scrumMasterService.getAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseScrumMasterDTO> updateScrumMaster(@PathVariable Long id, @Valid @RequestBody UpdateScrumMasterDTO scrumMasterDTO)
    {
        return new ResponseEntity<>(
                scrumMasterService.update(id, scrumMasterDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scrumMasterService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
