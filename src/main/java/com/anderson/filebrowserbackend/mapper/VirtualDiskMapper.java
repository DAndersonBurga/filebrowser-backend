package com.anderson.filebrowserbackend.mapper;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskRequestDto;
import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;
import com.anderson.filebrowserbackend.domain.model.VirtualDisk;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class VirtualDiskMapper {

    private final ModelMapper mapper;

    public VirtualDiskMapper(ModelMapper mapper) {
        this.mapper = mapper;
        configureMappings();
    }

    private void configureMappings() {
        Converter<VirtualDiskRequestDto, VirtualDisk> dtoToEntityConverter = context -> {
            VirtualDiskRequestDto source = context.getSource();
            VirtualDisk destination = context.getDestination() == null ? new VirtualDisk() : context.getDestination();
            destination.setLabel(source.label());
            destination.setName(source.name());
            destination.setDescription(source.description());
            destination.setFileType(source.filetype());
            return destination;
        };

        mapper.addConverter(dtoToEntityConverter, VirtualDiskRequestDto.class, VirtualDisk.class);
    }

    public VirtualDisk createVirtualDisk(VirtualDiskRequestDto request) {
        return mapper.map(request, VirtualDisk.class);
    }

    public VirtualDiskSummaryResponseDto fromVirtualDisk(VirtualDisk virtualDisk) {
        return mapper.map(virtualDisk, VirtualDiskSummaryResponseDto.class);
    }

}
