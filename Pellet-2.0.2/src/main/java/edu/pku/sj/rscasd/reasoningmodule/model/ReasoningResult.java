package edu.pku.sj.rscasd.reasoningmodule.model;

import java.io.Serializable;

import org.semanticweb.owl.model.OWLNamedObject;

import edu.pku.sj.rscasd.reasoningmodule.constant.MatchLevel;
import edu.pku.sj.rscasd.reasoningmodule.constant.ResultItemType;

public class ReasoningResult implements Serializable {

	private static final long serialVersionUID = -2455574293210679915L;

	private String shortExpression;

	private String expression;

	private String qualifiedUri;

	private ResultItemType resultType;

	private MatchLevel matchLevel;

	private Integer distance = Integer.MAX_VALUE;

	private String namespace;

	private OWLNamedObject source;

	public ReasoningResult() {
		this(null, null, null, ResultItemType.UNKNOWN, null, null);
	}

	public ReasoningResult(String shortExpression, String expression, String qualifiedUri, ResultItemType resultType,
			String namespace, OWLNamedObject source, MatchLevel level, Integer distance) {
		this.shortExpression = shortExpression;
		this.expression = expression;
		this.qualifiedUri = qualifiedUri;
		this.resultType = (resultType == null ? ResultItemType.UNKNOWN : resultType);
		this.namespace = namespace;
		this.source = source;
		this.matchLevel = level;
		this.distance = distance;
	}

	public ReasoningResult(String shortExpression, String expression, String qualifiedUri, ResultItemType resultType,
			String namespace, OWLNamedObject source) {
		this(shortExpression, expression, qualifiedUri, resultType, namespace, source, MatchLevel.NOMATCH,
				Integer.MAX_VALUE);
	}

	public ReasoningResult(String shortExpression, String expression, String qualifiedUri, String resultType,
			String namespace, OWLNamedObject source) {
		this(shortExpression, expression, qualifiedUri, ResultItemType.fromString(resultType), namespace, source);
	}

	public String getShortExpression() {
		return shortExpression;
	}

	public String getExpression() {
		return expression;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getQualifiedUri() {
		return qualifiedUri;
	}

	public ResultItemType getResultType() {
		return resultType;
	}

	public OWLNamedObject getSource() {
		return source;
	}

	public MatchLevel getMatchLevel() {
		return matchLevel;
	}

	public void setMatchLevel(MatchLevel matchLevel) {
		this.matchLevel = matchLevel;
	}

	public Integer getDistance() {
		if (distance == null) {
			distance = Integer.MAX_VALUE;
		}
		return distance == null ? -1 : distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public void setShortExpression(String shortExpression) {
		this.shortExpression = shortExpression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setQualifiedUri(String qualifiedUri) {
		this.qualifiedUri = qualifiedUri;
	}

	public void setResultType(ResultItemType resultType) {
		this.resultType = resultType;
	}

	public void setSource(OWLNamedObject source) {
		this.source = source;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result type[" + resultType.name() + "]=>");
		builder.append(expression + "(" + qualifiedUri + ")$");
		builder.append("[Source: " + source + "]");
		builder.append("[Match level: " + matchLevel.name() + "]");
		builder.append("[Distance: " + distance + "]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((matchLevel == null) ? 0 : matchLevel.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((qualifiedUri == null) ? 0 : qualifiedUri.hashCode());
		result = prime * result + ((resultType == null) ? 0 : resultType.hashCode());
		result = prime * result + ((shortExpression == null) ? 0 : shortExpression.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReasoningResult other = (ReasoningResult) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (matchLevel == null) {
			if (other.matchLevel != null)
				return false;
		} else if (!matchLevel.equals(other.matchLevel))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (qualifiedUri == null) {
			if (other.qualifiedUri != null)
				return false;
		} else if (!qualifiedUri.equals(other.qualifiedUri))
			return false;
		if (resultType == null) {
			if (other.resultType != null)
				return false;
		} else if (!resultType.equals(other.resultType))
			return false;
		if (shortExpression == null) {
			if (other.shortExpression != null)
				return false;
		} else if (!shortExpression.equals(other.shortExpression))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
