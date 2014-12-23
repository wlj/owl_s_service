package edu.pku.sj.rscasd.reasoningmodule.service.reasoner;

import java.net.URI;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import uk.ac.manchester.cs.owl.OWLOntologyURIMapperImpl;
import edu.pku.sj.rscasd.reasoningmodule.model.ReasoningResult;
import edu.pku.sj.rscasd.reasoningmodule.serviceimpl.reasoner.WeightedReasoningServiceImpl;

public class WeightedReasoningServiceTest {

	private final static Log logger = LogFactory.getLog(WeightedReasoningServiceTest.class);

	final String ns1 = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#";
	final String ns2 = "http://protege.stanford.edu/plugins/owl/owl-library/koala2.owl#";

	private ReasoningService reasoningService = new WeightedReasoningServiceImpl();

	@Test
	public void reasoning() {
		reasoningService.initialize();
		this.reasoningConcept(ns2, "SuperSchool");
		this.reasoningConcept(ns2, "AnotherSuperSchoolExample");
	}

	private void reasoningConcept(String namespace, String concept) {
		String srcUri = namespace + concept;
		Set<ReasoningResult> fulfilledExactMathings1 = reasoningService.getFulfilledExactMatchings(srcUri);
		Set<ReasoningResult> fulfilledExactMathings2 = reasoningService.getFulfilledExactMatchings(URI.create(srcUri));
		Set<ReasoningResult> fulfilledPluginMathings1 = reasoningService.getFulfilledPluginMatchings(srcUri);
		Set<ReasoningResult> fulfilledPluginMathings2 = reasoningService
				.getFulfilledPluginMatchings(URI.create(srcUri));
		Set<ReasoningResult> fulfilledSubsumeMathings1 = reasoningService.getFulfilledSubsumeMatchings(srcUri);
		Set<ReasoningResult> fulfilledSubsumeMathings2 = reasoningService.getFulfilledSubsumeMatchings(URI
				.create(srcUri));
		Set<ReasoningResult> fulfillingExactMathings1 = reasoningService.getFulfillingExactMatchings(srcUri);
		Set<ReasoningResult> fulfillingExactMathings2 = reasoningService
				.getFulfillingExactMatchings(URI.create(srcUri));
		Set<ReasoningResult> fulfillingPluginMathings1 = reasoningService.getFulfillingPluginMatchings(srcUri);
		Set<ReasoningResult> fulfillingPluginMathings2 = reasoningService.getFulfillingPluginMatchings(URI
				.create(srcUri));
		Set<ReasoningResult> fulfillingSubsumeMathings1 = reasoningService.getFulfillingSubsumeMatchings(srcUri);
		Set<ReasoningResult> fulfillingSubsumeMathings2 = reasoningService.getFulfillingSubsumeMatchings(URI
				.create(srcUri));

		logger.debug("\n=============== Start[" + concept + "(" + srcUri + ")] ================");
		logger.debug("Fulfilled Exact match 1: \n" + formatOutput(fulfilledExactMathings1));
		logger.debug("Fulfilled Exact match 2: \n" + formatOutput(fulfilledExactMathings2));
		logger.debug("Fulfilled Plugin match 1: \n" + formatOutput(fulfilledPluginMathings1));
		logger.debug("Fulfilled Plugin match 2: \n" + formatOutput(fulfilledPluginMathings2));
		logger.debug("Fulfilled Subsume match 1: \n" + formatOutput(fulfilledSubsumeMathings1));
		logger.debug("Fulfilled Subsume match 2: \n" + formatOutput(fulfilledSubsumeMathings2));
		logger.debug("Fulfilling Exact match 1: \n" + formatOutput(fulfillingExactMathings1));
		logger.debug("Fulfilling Exact match 2: \n" + formatOutput(fulfillingExactMathings2));
		logger.debug("Fulfilling Plugin match 1: \n" + formatOutput(fulfillingPluginMathings1));
		logger.debug("Fulfilling Plugin match 2: \n" + formatOutput(fulfillingPluginMathings2));
		logger.debug("Fulfilling Subsume match 1: \n" + formatOutput(fulfillingSubsumeMathings1));
		logger.debug("Fulfilling Subsume match 2: \n" + formatOutput(fulfillingSubsumeMathings2));
		logger.debug("=============== End[" + concept + "] ================");
		logger.debug("\n\n");
	}

	private String formatOutput(Set<ReasoningResult> results) {
		if (results == null) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		int i = 1;
		for (ReasoningResult result : results) {
			builder.append("\t[" + i + "]");
			builder.append(result.getResultType().name() + "  $$$  ");
			builder.append(result.getExpression() + "  $$$  ");
			builder.append(result.getMatchLevel() + "  $$$  ");
			builder.append(result.getDistance() + "  $$$  ");
			builder.append(result.getQualifiedUri() + "\n");
			i++;
		}

		return builder.toString();

	}
}
