/*
 * Copyright 2016 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.codecentric.springbootsample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    private RecordRepository repository;

    @Autowired
    public HomeController(RecordRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<Record> records = repository.findAll();
        model.addAttribute("records", records);
        model.addAttribute("insertRecord", new Record());
        return "home";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String insertData(ModelMap model, 
                             @ModelAttribute("insertRecord") @Valid Record record,
                             BindingResult result) {
        if (!result.hasErrors()) {
            repository.save(record);
        }
        return home(model);
    }

    @ResponseBody
    @RequestMapping(value = "/api/say/hello", method = RequestMethod.GET)
    public String sayHello(@RequestParam String to) {
        InetAddress inetAddress = null;
        String hostAddress = "";
        try {
            inetAddress = InetAddress.getLocalHost();
            hostAddress = inetAddress.getHostAddress();
            logger.info("Current IP address : " + inetAddress.getHostAddress());
        } catch (Exception e) {
            logger.info(String.valueOf(e));
        }

        if ( null == to ) {
            to = "nothing";
        }

        return "You said " + to + " to " + hostAddress + " date: " + new Date() + "\n";
    }
}
