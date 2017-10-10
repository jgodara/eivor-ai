package jgodara.ai.eivor.language;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import simplenlg.lexicon.XMLLexicon;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class LanguageProcessor {
	
	private static  StanfordCoreNLP NLP;
	private static  XMLLexicon XML_LEXICON;
	
	private static final String NNP_OR_PRP_PATTERN = ".*(NNP|PRP)";
	private static final String PRP$_PATTERN = ".*(PRP\\$)";
	private static final String NNP_PATTERN = ".*(NNP)";
	private static final String NN_PATTERN = ".*(NN)";
	
	public static void init() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		NLP = new StanfordCoreNLP(props);
		
		XML_LEXICON = new XMLLexicon("lexicons.xml");
	}
	
	public static ParsedSpeech parse(String sent) {
		ParsedSpeech parsedSpeech = null;
		Annotation document = new Annotation(sent);
		NLP.annotate(document);
		
		SemanticGraph depGraph = null;
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {			
			depGraph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}
		
		IndexedWord root = depGraph.getFirstRoot();
		String rootType = root.toString().substring(root.toString().lastIndexOf('/') + 1);
		String rootWord = root.word();
		
//		System.out.println(depGraph);
		
		if ("WP".equalsIgnoreCase(rootType)) {
			parsedSpeech = new ParsedSpeech(SpeechType.WHAT);
			parsedSpeech.addPos(ParsedSpeech.ROOT, rootWord);
			parsedSpeech.addPos(ParsedSpeech.SUBJECT, getAllNodesByWordPattern(depGraph, NNP_OR_PRP_PATTERN));
		} else if ("NNP".equalsIgnoreCase(rootType)) {
			rootWord = findPrp$InDependencyGraph(depGraph);
			
			parsedSpeech = new ParsedSpeech(SpeechType.FACT);
			parsedSpeech.addPos(ParsedSpeech.ROOT, rootWord);
			parsedSpeech.addPos(ParsedSpeech.F_ATTRIB_NAME, getAllNodesByWordPattern(depGraph, NN_PATTERN));
			parsedSpeech.addPos(ParsedSpeech.F_ATTRIB_VAL, getAllNodesByWordPattern(depGraph, NNP_PATTERN));
		} else if ("VB".equals(rootType)) {
			// TODO: Add parsing rule for verbs.
		} else {
			parsedSpeech = new ParsedSpeech(SpeechType.UNKOWN);
		}
		
		return parsedSpeech;
	}
	
	private static String findPrp$InDependencyGraph(SemanticGraph depGraph) {
		return getAllNodesByWordPattern(depGraph, PRP$_PATTERN);
	}

	private static String getAllNodesByWordPattern(SemanticGraph depGraph, String pattern) {
		Pattern p = Pattern.compile(pattern);
		List<String> nodes = new ArrayList<String>();
		
		Iterator<SemanticGraphEdge> edgesIter = depGraph.outgoingEdgeIterator(depGraph.getFirstRoot());
		while (edgesIter.hasNext()) {
			SemanticGraphEdge edge = edgesIter.next();
			IndexedWord vertex = edge.getSource();
			String w = vertex.toString() + " (" + edge.getRelation() + ")";
			if ((w == null && pattern == null) || w != null
					&& p.matcher(w).matches()) {
				nodes.add(vertex.word());
			}
		}

		String r = "";
		for (String v : nodes)
			r += v + " ";
		return r.trim();	
	}

}
