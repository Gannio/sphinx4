/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */
package tests.result.lattice;

import edu.cmu.sphinx.frontend.util.StreamDataSource;

import edu.cmu.sphinx.recognizer.Recognizer;

import edu.cmu.sphinx.result.ConfidenceResult;
import edu.cmu.sphinx.result.ConfidenceScorer;
import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.LatticeOptimizer;
import edu.cmu.sphinx.result.MAPConfidenceScorer;
import edu.cmu.sphinx.result.Result;

import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MAPConfidenceTest {

    /**
     * Main method for running the MAPConfidenceTest demo.
     */
    public static void main(String[] args) {
        try {            
            URL audioFileURL;
	    if (args.length > 0) {
                audioFileURL = new File(args[0]).toURI().toURL();
            } else {
                audioFileURL = new File("green.wav").toURI().toURL();
            }
            URL configURL = new File("./config.xml").toURI().toURL();

            System.out.println("Loading Recognizer...\n");

            ConfigurationManager cm = new ConfigurationManager(configURL);
	    Recognizer recognizer = (Recognizer) cm.lookup("recognizer");

            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();

	    StreamDataSource reader
		= (StreamDataSource) cm.lookup("streamDataSource");

            /* set the stream data source to read from the audio file */
	    reader.setInputStream
		(AudioSystem.getAudioInputStream(audioFileURL),
		 audioFileURL.getFile());

            /* decode the audio file */
            Result result = recognizer.recognize();
            
            /* print out the results */
            if (result != null) {
                ConfidenceScorer cs = new MAPConfidenceScorer();
                ConfidenceResult cr = cs.score(result);
                System.out.println(cr.getBestHypothesis().getTranscription() +
                                   ": " +
                                   cr.getBestHypothesis().getConfidence());
            }
        } catch (IOException e) {
            System.err.println("Problem when loading MAPConfidenceTest: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring MAPConfidenceTest: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating MAPConfidenceTest: " + e);
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio file format not supported: " + e);
            e.printStackTrace();
        }
    }
}