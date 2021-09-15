package bordero.backend;

import bordero.backend.controller.DTOModelMapper;
import bordero.backend.controller.DozerDTOModelMapper;
import bordero.backend.model.Bordero;
import bordero.backend.model.Customer;
import bordero.backend.model.Performance;
import bordero.backend.model.Play;
import bordero.dto.BorderoDTO;
import bordero.dto.CustomerDTO;
import bordero.dto.PerformanceDTO;
import bordero.dto.PlayDTO;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Mapper dozerMapper() {
        return DozerBeanMapperBuilder.create()
                .withMappingFiles("mapping.xml")
                .build();
    }

    @Bean
    public DTOModelMapper<BorderoDTO, Bordero> borderoMapper() {
        return new DozerDTOModelMapper<>(BorderoDTO.class, Bordero.class);
    }

    @Bean
    public DTOModelMapper<PlayDTO, Play> playMapper() {
        return new DozerDTOModelMapper<>(PlayDTO.class, Play.class);
    }


    @Bean
    public DTOModelMapper<PerformanceDTO, Performance> performanceMapper() {
        return new DozerDTOModelMapper<>(PerformanceDTO.class, Performance.class);
    }

    @Bean
    public DTOModelMapper<CustomerDTO, Customer> customerMapper() {
        return new DozerDTOModelMapper<>(CustomerDTO.class, Customer.class);
    }

}
