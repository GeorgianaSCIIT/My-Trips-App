package ro.siit.travel.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import ro.siit.travel.service.FileService;

@Controller
public class FileController {

        @Autowired
        private FileService fileService;

        @GetMapping(value ="/files/{filename:.+}", produces = MediaType.IMAGE_PNG_VALUE)
        @ResponseBody
        public ResponseEntity<Resource> getFile(@PathVariable String filename) {

            Resource file = fileService.loadAsResource(filename);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"temporaryFilename.png\"").body(file);
        }
}
