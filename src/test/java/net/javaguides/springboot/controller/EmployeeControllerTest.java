package net.javaguides.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;

import net.javaguides.springboot.exception.ResourceNotFoundException;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {EmployeeController.class})
@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepository employeeRepository;

    /**
     * Method under test: {@link EmployeeController#deleteEmployee(long)}
     */
    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        doNothing().when(employeeRepository).delete((Employee) any());
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        ResponseEntity<HttpStatus> actualDeleteEmployeeResult = employeeController.deleteEmployee(123L);
        assertNull(actualDeleteEmployeeResult.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualDeleteEmployeeResult.getStatusCode());
        assertTrue(actualDeleteEmployeeResult.getHeaders().isEmpty());
        verify(employeeRepository).findById((Long) any());
        verify(employeeRepository).delete((Employee) any());
    }

    /**
     * Method under test: {@link EmployeeController#deleteEmployee(long)}
     */
    @Test
    void testDeleteEmployee2() {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        doThrow(new ResourceNotFoundException("An error occurred")).when(employeeRepository).delete((Employee) any());
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeController.deleteEmployee(123L));
        verify(employeeRepository).findById((Long) any());
        verify(employeeRepository).delete((Employee) any());
    }

    /**
     * Method under test: {@link EmployeeController#deleteEmployee(long)}
     */
    @Test
    void testDeleteEmployee3() {
        doNothing().when(employeeRepository).delete((Employee) any());
        when(employeeRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeController.deleteEmployee(123L));
        verify(employeeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EmployeeController#createEmployee(Employee)}
     */
    @Test
    void testCreateEmployee() throws Exception {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(employee);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link EmployeeController#createEmployee(Employee)}
     */
    @Test
    void testCreateEmployee2() throws Exception {
        when(employeeRepository.findAll()).thenThrow(new ResourceNotFoundException("An error occurred"));

        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(employee);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link EmployeeController#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link EmployeeController#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees2() throws Exception {
        when(employeeRepository.findAll()).thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link EmployeeController#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees3() throws Exception {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/becquerel/employees");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link EmployeeController#getEmployeeById(long)}
     */
    @Test
    void testGetEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees/{id}", 123L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"emailId\":\"42\",\"id\":123}"));
    }

    /**
     * Method under test: {@link EmployeeController#getEmployeeById(long)}
     */
    @Test
    void testGetEmployeeById2() throws Exception {
        when(employeeRepository.findById((Long) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link EmployeeController#getEmployeeById(long)}
     */
    @Test
    void testGetEmployeeById3() throws Exception {
        when(employeeRepository.findById((Long) any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/becquerel/employees/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link EmployeeController#updateEmployee(long, Employee)}
     */
    @Test
    void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee1 = new Employee();
        employee1.setEmailId("42");
        employee1.setFirstName("Jane");
        employee1.setId(123L);
        employee1.setLastName("Doe");
        when(employeeRepository.save((Employee) any())).thenReturn(employee1);
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);

        Employee employee2 = new Employee();
        employee2.setEmailId("42");
        employee2.setFirstName("Jane");
        employee2.setId(123L);
        employee2.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(employee2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/becquerel/employees/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"emailId\":\"42\",\"id\":123}"));
    }

    /**
     * Method under test: {@link EmployeeController#updateEmployee(long, Employee)}
     */
    @Test
    void testUpdateEmployee2() throws Exception {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.save((Employee) any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(employeeRepository.findById((Long) any())).thenReturn(ofResult);

        Employee employee1 = new Employee();
        employee1.setEmailId("42");
        employee1.setFirstName("Jane");
        employee1.setId(123L);
        employee1.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(employee1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/becquerel/employees/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link EmployeeController#updateEmployee(long, Employee)}
     */
    @Test
    void testUpdateEmployee3() throws Exception {
        Employee employee = new Employee();
        employee.setEmailId("42");
        employee.setFirstName("Jane");
        employee.setId(123L);
        employee.setLastName("Doe");
        when(employeeRepository.save((Employee) any())).thenReturn(employee);
        when(employeeRepository.findById((Long) any())).thenReturn(Optional.empty());

        Employee employee1 = new Employee();
        employee1.setEmailId("42");
        employee1.setFirstName("Jane");
        employee1.setId(123L);
        employee1.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(employee1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/becquerel/employees/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

