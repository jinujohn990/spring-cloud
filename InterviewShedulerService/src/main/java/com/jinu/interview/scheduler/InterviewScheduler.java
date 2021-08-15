package com.jinu.interview.scheduler;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jinu.interview.model.Candidate;
import com.jinu.interview.model.EmpIoyeeidGroup;
import com.jinu.interview.model.InterviewSchedule;
import com.jinu.interview.model.Interviewer;
import com.jinu.interview.repository.CandidateRepository;
import com.jinu.interview.repository.InterviewScheduleRepository;
import com.jinu.interview.repository.InterviewerRepository;
import com.jinu.interview.util.InterviewUtils;
@ConditionalOnProperty(prefix = "scheduler", name = "enable", havingValue = "true")
@Component
public class InterviewScheduler {
	Logger logger = LoggerFactory.getLogger(InterviewScheduler.class);
	@Autowired
	CandidateRepository candidateRepository;
	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;
	@Autowired
	InterviewerRepository interviewerRepository;
	//@Scheduled(cron = "* * * * * *" )
	@Scheduled(cron = "0 */1 * ? * *" )
	public synchronized void scheduleInterviewForcandidate(){
		logger.info("Entering scheduleInterviewForcandidate");
		scheduleInterviewForcandidateForRoundOne();
		scheduleInterviewForRoundTwo();
		scheduleInterviewForRoundThree();
		logger.info("Exiting scheduleInterviewForcandidate");
	}

	public void scheduleInterviewForcandidateForRoundOne() {
		logger.info("Entering scheduleInterviewForcandidateForRoundOne");
		List<Candidate> newCandidates = null ;
		synchronized (this) {
			 newCandidates = candidateRepository.getCandidateByStatus("new candidate");
		}
		//List<Candidate> newCandidates = candidateRepository.getCandidateByStatus("new candidate");
		if(!InterviewUtils.isListNullOrEmpty(newCandidates)) {
			LocalDate localdate = LocalDate.now();
			localdate = localdate.plusDays(2);
			Date date = Date.valueOf(localdate);
			int round = 1;
			sheduleInterviewForTheNextRound(newCandidates, date, round);	
		}
		logger.info("Entering scheduleInterviewForcandidateForRoundOne");

	}
	public synchronized void scheduleInterviewForRoundTwo() {
		logger.info("Entering scheduleInterviewForRoundTwo");
		List<Candidate> roudOneCandidates = candidateRepository.getCandidateByStatus("roundone_completed");
		if(!InterviewUtils.isListNullOrEmpty(roudOneCandidates)) {
			List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findByCandidateAndRound(roudOneCandidates.get(0), 1);
			LocalDate localdate = interviewSchedules.get(0).getInterviewDate().toLocalDate();
			localdate = localdate.plusDays(2);
			Date date = Date.valueOf(localdate);
			int round = 2;
			sheduleInterviewForTheNextRound(roudOneCandidates, date, round);
		}
		logger.info("Exiting scheduleInterviewForRoundTwo");
	}

	public synchronized void scheduleInterviewForRoundThree() {
		logger.info("Entering scheduleInterviewForRoundThree");
		List<Candidate> roudTwoCandidates = candidateRepository.getCandidateByStatus("roundtwo_completed");
		if(!InterviewUtils.isListNullOrEmpty(roudTwoCandidates)) {
			LocalDate localdate = LocalDate.now();
			localdate = localdate.plusDays(2);
			Date date = Date.valueOf(localdate);
			int round = 3;
			sheduleInterviewForTheNextRound(roudTwoCandidates, date, round);
		}		
		logger.info("Exiting scheduleInterviewForRoundThree");
	}

	private  void sheduleInterviewForTheNextRound(List<Candidate> newCandidates, Date date, int round) {
		logger.info("Entering sheduleInterviewForTheNextRound");
		List<InterviewSchedule> interviewSchedules;
		Interviewer randomInterviewer;
		LocalDate localdate;
		for (Candidate candidate : newCandidates) {
			interviewSchedules = interviewScheduleRepository.findByInterviewDate(date);
			if(interviewSchedules == null || interviewSchedules.isEmpty()) {
				 randomInterviewer = interviewerRepository.findRandomInterviewer();
				allocatedTimeAndScheduleInterviewWithRandomInterviewer(randomInterviewer,candidate,date,round);
			}
			else {
				List<Long> interviewersIds = interviewSchedules.stream().map(e -> e.getInterviewer().getInterviewerId()).collect(Collectors.toList());
				List<Interviewer> interviewersAvailable = interviewerRepository.findByEmpIdNotIn(interviewersIds);
				if (interviewersAvailable != null && !interviewersAvailable.isEmpty()) {
					Optional<Interviewer> interviewerOptional = interviewersAvailable.stream().findAny();
					Interviewer interviewerAvailable = interviewerOptional.get();
					InterviewSchedule interviewSchedule = new InterviewSchedule();
					interviewSchedule.setCandidate(candidate);
					interviewSchedule.setInterviewer(interviewerAvailable);
					interviewSchedule.setInterviewDate(date);
					interviewSchedule.setRound(round);
					interviewSchedule.setTimeSchedule("9-11");
					interviewScheduleRepository.save(interviewSchedule);
					candidate.setStatus("scheduled_interview_round"+round);
					candidateRepository.save(candidate);
				} else {
					List<EmpIoyeeidGroup> empIoyeeidGroups = interviewScheduleRepository.groupByInterviewer(date);
					if (InterviewUtils.isListNullOrEmpty(empIoyeeidGroups)) {
						localdate = date.toLocalDate();
						localdate = localdate.plusDays(1);
						date = Date.valueOf(localdate);
						while (true) {
							interviewSchedules = interviewScheduleRepository.findByInterviewDate(date);
							empIoyeeidGroups = interviewScheduleRepository.groupByInterviewer(date);
							if (InterviewUtils.isListNullOrEmpty(empIoyeeidGroups)
									&& !InterviewUtils.isListNullOrEmpty(interviewSchedules)) {
								localdate = date.toLocalDate();
								localdate = localdate.plusDays(1);
								date = Date.valueOf(localdate);
								continue;
							}
							else if(!InterviewUtils.isListNullOrEmpty(empIoyeeidGroups)) {
								allocateInterviewWithMinimumAllocatedInterviewer(date, round, candidate,empIoyeeidGroups);
								break;
							}
							randomInterviewer = interviewerRepository.findRandomInterviewer();
							allocatedTimeAndScheduleInterviewWithRandomInterviewer(randomInterviewer, candidate, date, round);
							break;
						}
					}else {
						allocateInterviewWithMinimumAllocatedInterviewer(date, round, candidate, empIoyeeidGroups);
					}
				}
			} 
		}
		logger.info("Exiting sheduleInterviewForTheNextRound");
	}

	private void allocateInterviewWithMinimumAllocatedInterviewer(Date date, int round, Candidate candidate,
			List<EmpIoyeeidGroup> empIoyeeidGroups) {
		List<InterviewSchedule> interviewSchedules;
		EmpIoyeeidGroup empIoyeeidGroup = empIoyeeidGroups.get(0);
		Interviewer interviewer = empIoyeeidGroup.getInterviewer();
		interviewSchedules = interviewScheduleRepository.findByInterviewDateAndInterviewer(interviewer, date);
		allocatedTimeAndScheduleInterview(interviewSchedules, candidate, date, round);
	}

	private  void allocatedTimeAndScheduleInterviewWithRandomInterviewer(Interviewer randomInterviewer,Candidate candidate, Date date,int round) {
		logger.info("Entering allocatedTimeAndScheduleInterview");
		InterviewSchedule interviewSchedule = new InterviewSchedule();
		interviewSchedule.setCandidate(candidate);
		interviewSchedule.setInterviewer(randomInterviewer);
		interviewSchedule.setInterviewDate(date);
		interviewSchedule.setRound(round);
		interviewSchedule.setTimeSchedule("9-11");
		interviewScheduleRepository.save(interviewSchedule);
		candidate.setStatus("scheduled_interview_round"+round);
		candidateRepository.save(candidate);
		logger.info("Exiting allocatedTimeAndScheduleInterview");
	}

	private void allocatedTimeAndScheduleInterview(List<InterviewSchedule> interviewSchedules, Candidate candidate,Date date,int round) {
		logger.info("Entering allocatedTimeAndScheduleInterview");
		InterviewSchedule interviewSchedule = new InterviewSchedule();
		interviewSchedule.setCandidate(candidate);
		interviewSchedule.setInterviewer(interviewSchedules.get(0).getInterviewer());
		interviewSchedule.setInterviewDate(date);
		interviewSchedule.setRound(round);
		interviewSchedule.setTimeSchedule(allocateTime(interviewSchedules));
		interviewScheduleRepository.save(interviewSchedule);
		candidate.setStatus("scheduled_interview_round"+round);
		candidateRepository.save(candidate);
		logger.info("Exiting allocatedTimeAndScheduleInterview");
	}

	private String allocateTime(List<InterviewSchedule> interviewSchedules) {
		logger.info("Entering allocateTime");
		List<String> timeScheluelist = interviewSchedules.stream().map(e -> e.getTimeSchedule())
				.collect(Collectors.toList());
		if (!timeScheluelist.contains("9-11")) {
			logger.info("Exiting allocateTime");
			return "9-11";
		}
		if (!timeScheluelist.contains("11-1")) {
			logger.info("Exiting allocateTime");
			return "11-1";
		} else {
			logger.info("Exiting allocateTime");
			return "2-4";
		}
	
	}

	

}
