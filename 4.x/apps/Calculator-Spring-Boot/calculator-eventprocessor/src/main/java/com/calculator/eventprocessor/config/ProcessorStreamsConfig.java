package com.calculator.eventprocessor.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import com.calculator.eventprocessor.stream.ProcessorStreams;

@EnableBinding(ProcessorStreams.class)
public class ProcessorStreamsConfig {

}