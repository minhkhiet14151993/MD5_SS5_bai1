package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.model.entity.Song;
import ra.service.ISongService;
import ra.serviceImpl.SongServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Controller
public class SongController {
    @Autowired
    private ISongService songService;
    @GetMapping("/")
    public String listSong(Model model){
        List<Song> songList = songService.findAll();
        model.addAttribute("list",songList);
        return "list";
    }
    @GetMapping("/createForm")
    public String formCreate(Model model){
        Song song= new Song();
        model.addAttribute("song",song);
        return "formCreate";
    }
    @PostMapping("/create")
    public String formAdd(Model model, @RequestParam("file") MultipartFile files, @ModelAttribute("song") Song song) throws IOException {
        String uploadPath = "/Users/minhkhiet/javaMD3DINHVANKHIET/bai1/src/main/resources/assets/files/";
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = files.getOriginalFilename();
        FileCopyUtils.copy(files.getBytes(), new File(uploadPath + File.separator + fileName));
        model.addAttribute("fileName", fileName);
        Song songC = new Song(song.getSongName(),song.getAuthor(),song.getCatalog(),fileName);
        songService.save(songC);
        return "redirect:/";
    }
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id")int id){
        songService.delete(id);
        return "redirect:/";
    }
    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id")int id,Model model){
       Song song =  songService.findById(id);
       model.addAttribute("song",song);
       return "edit";
    }
    @PostMapping("update")
    public String update(@ModelAttribute("song")Song song){
        songService.save(song);
        return "redirect:/";
    }


}
