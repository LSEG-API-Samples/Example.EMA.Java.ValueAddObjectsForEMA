/*
 * Copyright 2017 Thomson Reuters
 *
 * DISCLAIMER: This source code has been written by Thomson Reuters for the only 
 * purpose of illustrating articles published on the Thomson Reuters Developer 
 * Community. It has not been tested for usage in production environments. 
 * Thomson Reuters cannot be held responsible for any issues that may happen if 
 * these objects or the related source code is used in production or any other 
 * client environment.
 *
 * Thomson Reuters Developer Community: https://developers.thomsonreuters.com
 *
 */
package com.thomsonreuters.platformservices.elektron.objects.chain;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Holds the number of summary links to skip for different Display 
 * Templates used by chains. 
 * <br>
 * Some chains start with summary links (elements) that you may want to skip 
 * because they are not part of the chain's constituents. The number of 
 * summary links to skip depends on the Display Template used by the chain. 
 * This class holds this information that can be passed to a FlatChain or a 
 * RecursiveChain as a parameter. Checkout this <a href="https://developers.thomsonreuters.com/platform-services-work-progress/ema/docs?content=12021&type=documentation_item" target="_blank">Thomson Reuters article</a> 
 * to learn more about chains and summary links.
 * <br>
 * To find out the number of summary links and the display template used by a 
 * given chain, you must open the chain in a consumer application that displays
 * the chain's fields and their values. We recommend you to read this Checkout 
 * this <a href="https://developers.thomsonreuters.com/platform-services-work-progress/ema/docs?content=12021&type=documentation_item" target="_blank">article</a>
 * to understand how to determine the display template used by the chain.<br>
 * As examples:<br>
 * <ul>
 *     <li>0#.DJI uses Display Template 187 and has 1 summary link to skip</li>
 *     <li>0#.FTSE uses Display Template 205 and has 2 summary links to skip</li>
 *     <li>0#.FTMIB uses Display Template 1792 and has 6 summary links to skip</li>
 *     <li>0#.FCHI uses Display Template 1098 and has 6 summary links to skip</li>
 * </ul>
 */
public class SummaryLinksToSkipByDisplayTemplate
{
    private Map<Long, Integer> linksToSkipByDisplayTemplate;
    
    private SummaryLinksToSkipByDisplayTemplate(Builder builder)
    {
        this.linksToSkipByDisplayTemplate = builder.linksToSkipByDisplayTemplate;
    }
        
    /**
     * Returns the number of summary links to skip for the given Display Template 
     * Id.
     * @param displayTemplateId Id of the display template.
     * @return the number of summary links to skip.
     */
    public int getLinksToSkipForDisplayTemplate(long displayTemplateId)
    {
        if(linksToSkipByDisplayTemplate.containsKey(displayTemplateId))
        {
            return linksToSkipByDisplayTemplate.get(displayTemplateId);
        }
        return 0;
    }
    
    /**
     * Used to build <code>SummaryLinksToSkipByDisplayTemplate</code> objects. 
     * <code>SummaryLinksToSkipByDisplayTemplate</code>s are immutable objects. 
     * This means that you can't their values and state once they are built 
     * (no setters).<br>
     * The following code snippet builds a <code>FlatChain</code> for the British
     * FTSE 100 and set the two functions (lambda expressions in this case) to 
     * be called when the chain is complete and when an error occurs:
     * <br>
     * <br>
     * As an example, the following code snippet creates a such an object that 
     * indicates the number of summary links to skip for DIsplay Templates 178, 205,
     * 1792 and 1098. 
     * <br>
     * <br>
     * <pre>
     *        SummaryLinksToSkipByDisplayTemplate summaryLinksToSkip = new SummaryLinksToSkipByDisplayTemplate.Builder()
     *               .forDisplayTemplate(187).skip(1)  // e.g. 0#.DJI
     *               .forDisplayTemplate(205).skip(2)  // e.g. 0#.FTSE
     *               .forDisplayTemplate(1792).skip(6) // e.g. 0#.FTMIB
     *               .forDisplayTemplate(1098).skip(6) // e.g. 0#.FCHI
     *               .build();
     * </pre>
     */    
    public static class Builder
    {   
        private DisplayTemplateDescription displayTemplateDescription;
        private Map<Long, Integer> linksToSkipByDisplayTemplate = new HashMap<>();

        /**
         * Default constructor
         */        
        public Builder()
        {
            displayTemplateDescription = new DisplayTemplateDescription(this);
        }

        /**
         * Builds the <code>SummaryLinksToSkipByDisplayTemplate</code>
         * @return the built object
         */        
        public SummaryLinksToSkipByDisplayTemplate build()
        {

            SummaryLinksToSkipByDisplayTemplate summaryLinksToSkip = new SummaryLinksToSkipByDisplayTemplate(this);
            return summaryLinksToSkip;
        }
        
        /**
         * Returns the <code>DisplayTemplateDescription</code> of the given Id.
         * @param displayTemplateId the Id of the requested DisplayTemplateDescription
         * @return the DisplayTemplateDescription.
         */
        public DisplayTemplateDescription forDisplayTemplate(long displayTemplateId)
        {
            displayTemplateDescription.setDisplayTemplateId(displayTemplateId);
            return displayTemplateDescription;
        }
        
        private void setLinksToSkipForDisplayTemplate(long displayTemplateId, int linksToSkipCount)
        {
            linksToSkipByDisplayTemplate.put(displayTemplateId, linksToSkipCount);
        }
        
    }
    
    /**
     * Holds a Display Template description.
     */
    public static class DisplayTemplateDescription
    {
        private Builder builder;
        private long displayTemplateId;
                
        private DisplayTemplateDescription(Builder builder)
        {
            this.builder = builder;
        }
                
        /**
         * Sets the number of summary links to skip for this Display Template 
         * description.
         * @param linksCountToSkip number of summary links to skip
         * @return the SummaryLinksToSkipByDisplayTemplate.Builder so that you 
         * can chain methods calls.
         */
        public Builder skip(int linksCountToSkip)
        {
            builder.setLinksToSkipForDisplayTemplate(displayTemplateId, linksCountToSkip);
            return builder;
        }
        
        private void setDisplayTemplateId(long displayTemplateId)
        {
            this.displayTemplateId = displayTemplateId;
        }
        
    }
}
