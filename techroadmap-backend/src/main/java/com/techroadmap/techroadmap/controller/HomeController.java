package com.techroadmap.techroadmap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techroadmap.techroadmap.Entity.RoadMapObjectDetails;
import com.techroadmap.techroadmap.repository.RoadMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/roadmap")
public class HomeController {

    @Autowired
    RoadMapRepository roadMapRepository;

    @GetMapping("/home")
    public String home() {
        return "Welcome";
    }

    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadDetails(@RequestParam MultipartFile file,
                                @RequestParam String keywords,
                                @RequestParam String content,
                                @RequestParam String techName) throws IOException {

        RoadMapObjectDetails detail = new RoadMapObjectDetails();
        detail.setContent(content);
        detail.setId(String.valueOf(UUID.randomUUID()));
        detail.setKeywords(keywords);
        detail.setFileName(techName);
        detail.setTechName(techName);
        detail.setPdfFile(file.getBytes());

        roadMapRepository.save(detail);

        return "Success";

    }

    @GetMapping("/techs")
    public String getAllTechsAvailable() throws JsonProcessingException {
        Iterable<RoadMapObjectDetails> allAvailableTechs = roadMapRepository.findAll();

        Iterator<RoadMapObjectDetails> alltechs = allAvailableTechs.iterator();

        HashMap<UUID, String>  map  = new HashMap<UUID, String>();

        while(alltechs.hasNext()) {
            RoadMapObjectDetails details = alltechs.next();
            map.computeIfAbsent(UUID.fromString(details.getId()), techName -> details.getTechName());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }

    @GetMapping("/find")
    public String getTech(@RequestParam String techName,
                          @RequestParam String id) throws Exception {
        Optional<RoadMapObjectDetails> details = roadMapRepository.findById(id);

        if(details.isPresent()) {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(details);
        } else {
            throw new Exception();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> getPdf(@RequestParam String id,
                                         @RequestParam String techName) {
        Optional<RoadMapObjectDetails> details = roadMapRepository.findById(id);
        if(details.isPresent()) {
            RoadMapObjectDetails r = details.orElse(null);
            byte[] data = r.getPdfFile();

            InputStream inputStream = new ByteArrayInputStream(data);

            byte[] bytes;
            try {
                bytes = inputStream.readAllBytes();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition",
                            "attachment; filename=" + techName + ".pdf")
                    .body(bytes);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
