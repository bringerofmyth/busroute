package com.goeuro;

import com.goeuro.service.BusRouteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusRouteApplicationTests {

	@Autowired
	BusRouteService busRouteService ;

	@Test
	public void testRouteMap() {
		String testFile = "/Users/melihgurgah/IdeaProjects/BusRoute/src/main/resources/test.txt";
		busRouteService.initializeMap(testFile);
		assertEquals(true, busRouteService.isExists(2,6));
	}

}
