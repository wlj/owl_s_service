package cn.edu.pku.ss.matchmaker.similarity.io;

import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.Output;


public interface IOSimilarity {
	public double getInputSimilarity(Input srcInput, Input destInput);
	public double GetOutPutSimilarity(Output srcOutput, Output destOutput);
	public double GetIOSimilarity();
}
