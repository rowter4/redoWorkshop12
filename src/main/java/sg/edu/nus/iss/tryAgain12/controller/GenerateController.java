package sg.edu.nus.iss.tryAgain12.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;


import sg.edu.nus.iss.tryAgain12.model.Generate; // linking the Generate.java file to this file
import sg.edu.nus.iss.tryAgain12.exception.RandomNumberException;

@Controller
public class GenerateController {

        private Logger logger = LoggerFactory.getLogger(GenerateController.class);
        
        @GetMapping(path="/")
        public String showGenerateForm (Model model){
            Generate generate = new Generate(); // generate a new object from generate model
            model.addAttribute("generateObj", generate); // generateObj is linked to the generatePage.html
            return "generatePage"; // linked to the generatePage title

        }

        @PostMapping(path="/generate")  // what happens after the form is submitted
        public String generateNumbers(@ModelAttribute Generate generate, Model model){
           try {
            int numberRandomNumbers = generate.getNumberVal(); // return the number value that is stored within the generate model 
           
            if (numberRandomNumbers > 10) { // we only can generate up to 10 numbers. Therefore, this condition is required
                throw new RandomNumberException();
                // note! This condition to throw must be coded after the catch (RandomNumberException e) is coded
            }

            

            Random randNum = new Random(); // generate a random number
            Set<Integer> uniqueGeneratedNumSet = new LinkedHashSet<Integer>(); /* create a container to house the generated numbers 
                                                                                 and ensure that the numbers are not duplicated
                                                                               */
            while (uniqueGeneratedNumSet.size() < numberRandomNumbers) {
                Integer resultOfRandNumbers = randNum.nextInt(generate.getNumberVal() + 1);
                uniqueGeneratedNumSet.add(resultOfRandNumbers); // the data contained within uniqueGeneratedNumSet will contain the numbers
                                                                // that are not being duplicated
            }

            // Now, we need to link the numbers to the photo number that is present

            Iterator<Integer> it = uniqueGeneratedNumSet.iterator(); // iterator is being used to loop through collections
            Integer currentElem = null ; 

            String[] imgNumbers = {  // initialising all of the directories that are present in the folder
                "number1.jpg", "number2.jpg", "number3.jpg", "number4.jpg", "number5.jpg",
                "number6.jpg", "number7.jpg", "number8.jpg", "number9.jpg", "number10.jpg"
            };

            List<String> selectedImg = new ArrayList<String>(); // create a list to store the selected images

            while(it.hasNext()){ // .hasNext() will be returning true if there is a token in its input
                currentElem = it.next();
                selectedImg.add(imgNumbers[currentElem.intValue()]); // add the selected image into the list
            }

            model.addAttribute("randNumsResult", selectedImg.toArray());/* randNumsResult is inside result.html, 
                                                                           and will append the data from selectedImg
                                                                        */

            model.addAttribute("numInputByUser", numberRandomNumbers);  /* numberRandomNumbers will also be passed back to
                                                                          result.html
                                                                        */


           } catch(RandomNumberException e) {
                model.addAttribute("errorMessage", "Unable to display");
                return "error";
           }

           return "result";
        } 
}
