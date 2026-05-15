package com.smartbadge.adl.shared.config;

import com.smartbadge.adl.staff.Staff;
import com.smartbadge.adl.staff.StaffDto;
import com.smartbadge.adl.staff.StaffProfileDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.typeMap(Staff.class, StaffDto.class)
                .addMapping(Staff::getAddresses, StaffDto::setAddress);
        modelMapper.typeMap(Staff.class, StaffProfileDto.class)
                .addMapping(Staff::getAddresses, StaffProfileDto::setAddress);

        return modelMapper;
    }
}
