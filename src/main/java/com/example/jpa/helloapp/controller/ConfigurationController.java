package com.example.jpa.helloapp.controller;

import com.example.jpa.helloapp.model.Configuration;
import com.example.jpa.helloapp.repository.ConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/api")
@Slf4j
public class ConfigurationController {

    @Autowired
    ConfigurationRepository snPropertiesRepository;

    @GetMapping("/props")
    public ResponseEntity<List<Configuration>> getAllProperties(
            @RequestParam(required = false) String key) {
        try {

            List<Configuration> list = new ArrayList<>();
            if (key == null) {
                snPropertiesRepository.findAll().forEach(list::add);
            } else {
                snPropertiesRepository.findBykey(key).forEach(list::add);
            }
            if (list.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            log.info("Found properties list size : {}", list.size());
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/propsAsync")
    public DeferredResult<ResponseEntity<?>> getAllPropertiesAsync(
            @RequestParam(required = false) String key) {
        log.info("Received async-deferredresult request");

        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(1000L);
        output.onCompletion(() -> {
            log.info(">>> Result is ready");
        });
        output.onTimeout(() -> {
            log.error(">>> Result timeout");
            output.setErrorResult(new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT));
        });
        output.onError((Throwable t) -> {
            log.error(">>> Result error {}", t.getMessage());
            output.setErrorResult(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
        });


        ForkJoinPool.commonPool().submit(() -> {
            log.info("Processing in separate thread");

            try {
                List<Configuration> list = new ArrayList<>();
                if (key == null) {
                    snPropertiesRepository.findAll().forEach(list::add);
                } else {
                    snPropertiesRepository.findBykey(key).forEach(list::add);
                }
                if (list.isEmpty()) {
                    output.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
                }
                log.info("Found properties list size : {}", list.size());
                output.setResult(new ResponseEntity<>(list, HttpStatus.OK));

            } catch (Exception e) {
                output.setResult(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
            }

        });

        log.info("Servlet thread freed");

        return output;
    }
}
