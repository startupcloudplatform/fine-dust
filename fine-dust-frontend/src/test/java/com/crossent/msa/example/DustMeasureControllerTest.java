package com.crossent.msa.example;

import com.crossent.msa.example.configuration.DustMeasureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DustMeasureControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    private DustMeasureController dustMeasureController;

    @Mock
    private DustMeasureService dustMeasureService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dustMeasureController).build();
    }


    @Test
    public void dustAverageSido() throws Exception {
        //when(dustMeasureService.dustAverageSido("pm25")).thenReturn(DustMeasureConfiguration.setDustAverageSido());
        mockMvc.perform(get("/api/dust/measure/avg/sido").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}