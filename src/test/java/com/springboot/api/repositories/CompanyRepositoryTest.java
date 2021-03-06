package com.springboot.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import com.springboot.api.entities.Company;
import com.springboot.api.utils.Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * UserRepositoryTest
 */
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CompanyRepositoryTest {


    @Autowired
    private CompanyRepository companyRepository;

    private Company company;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(Utils.provideAuthentication());
        SecurityContextHolder.setContext(securityContext);
        
        this.pageable = PageRequest.of(0, 25, Sort.by(Order.asc("name"), Order.desc("id")));

        company = Company.builder().name("Company XYZ").build();
        this.companyRepository.save(company);
    }

    @AfterEach
    public void tearDown() {
        this.companyRepository.deleteAll();
    }

    
    @Test
    public void testFindCopmanyById() {
        Optional<Company> companyOp = this.companyRepository.findById(this.company.getId());
        assertNotNull(companyOp.get());
    }
    
    @Test
    public void testFindCopmanyByName() {
        List<Company> companies = this.companyRepository.findByName("Company XYZ", this.pageable);
        assertEquals(1, companies.size());
    }

}
