package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        List<TestRequest> testList = testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);
        return testList;

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        //Implement this method
        User Doctor = userLoggedInService.getLoggedInUser();
        List<TestRequest> testList = testRequestQueryService.findByDoctor(Doctor);
        return testList;

    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        // Implement this method
        try {
            // replace this line of code with your implementation
            User Doctor = userLoggedInService.getLoggedInUser();
            TestRequest testRequest = testRequestUpdateService.assignForConsultation(id, Doctor);
            return testRequest;

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        // Implement this method

        // Implement this method to update the result of the current test request id with test doctor comments
        // Create an object of the User class to get the logged in user
        // Create an object of TestRequest class and make use of updateConsultation() method from testRequestUpdateService class
        //to update the current test request id with the testResult details by the current user(object created)
        // For reference check the method updateLabTest() method from LabRequestController class

        try {
            // replace this line of code with your implementation
            User doctor = userLoggedInService.getLoggedInUser();
            TestRequest testRequest = testRequestUpdateService.updateConsultation(id, testResult, doctor);
            return testRequest;
        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}
