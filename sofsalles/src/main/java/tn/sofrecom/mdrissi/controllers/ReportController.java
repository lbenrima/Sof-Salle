package tn.sofrecom.mdrissi.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.dto.ReportDTO;
import tn.sofrecom.mdrissi.mail.SendReport;

@RestController
@RequestMapping(value = "/v1/report")
public class ReportController {
	
	@Autowired
	private SendReport sendReport;
	
	
	@PostMapping("/sendMail")
	public void SendMail(@RequestBody ReportDTO report) {
		try {
			sendReport.SendReportMail(report.getFrom(), report.getSubject(), report.getDescription());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
