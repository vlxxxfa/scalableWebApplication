package edu.hm.pam;

import com.fasterxml.jackson.databind.JsonMappingException;
import edu.hm.pam.entity.Photo;
import org.bson.json.JsonParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by vlfa on 03.04.17.
 */
@CrossOrigin(origins = "*")
@RestController
@MultipartConfig(fileSizeThreshold = 20971520)
@RequestMapping(value = "/")
public class Test {

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) throws JsonParseException, JsonMappingException, IOException {
    String result;

    System.out.println("MultipartFile is: " + file.getOriginalFilename());

        if (!file.isEmpty()) {
            try {
                result = "Upload worked";
            } catch (Exception e) {
                result = "failed to upload the file: " + file+ " => " + e.getMessage();
            }
        } else {
            result = "failed to upload the file: " + file + ", because the file was empty.";
        }

        // Photo photo = new Photo();
        // photo.setPhotoData(file.getBytes());
        // photo.setTitle(file.getOriginalFilename());
        return result;
    }


@RequestMapping(value = "uploadFile2", method = RequestMethod.POST)
    public String uploadFile2(HttpServletRequest servletRequest,
                              @ModelAttribute Photo photo,
                              Model model) {
    String result = "";

    MultipartFile file = photo.getMultipartFile();

    String fileName = file.getOriginalFilename();

    File imageFile = new File(servletRequest.getServletContext().getRealPath("/image"), fileName);
    try
    {
        file.transferTo(imageFile);
    } catch (IOException e)
    {
        e.printStackTrace();
    }

    System.out.println(fileName.toString());
    model.addAttribute("file", file);

        return result;
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String test(){
         return "Worked";

    }

}
