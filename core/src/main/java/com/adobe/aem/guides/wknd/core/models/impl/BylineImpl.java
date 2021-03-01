package com.adobe.aem.guides.wknd.core.models.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;

@Model(
	adaptables = {SlingHttpServletRequest.class},
	adapters = {Byline.class},
	resourceType = {BylineImpl.RESOURCE_TYPE},
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BylineImpl implements Byline {

	protected static final String RESOURCE_TYPE = "wknd/components/content/byline";

/*
    // This is the better way to get the image, but we are using the
    // Sling Model Factory (with @OSGIService) just to show how that's done.
	@Self
	private Image image;
*/
	@Self
	private SlingHttpServletRequest request;

	@OSGiService
	private ModelFactory modelFactory;

	@ValueMapValue
	private String name;

	@ValueMapValue
	private List<String> occupations;

	private Image image;

	@PostConstruct
	private void init() {
		image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getOccupations() {
		if (occupations != null) {
			Collections.sort(occupations);
			return new ArrayList<String>(occupations);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public boolean isEmpty() {
		final Image componentImage = getImage();

		if (StringUtils.isBlank(name)) {
			// Name is missing, but required
			return true;
		} else if (occupations == null || occupations.isEmpty()) {
			// At least one occupation is required
			return true;
		} else if (componentImage == null || StringUtils.isBlank(componentImage.getSrc())) {
			// A valid image is required
			return true;
		} else {
			// Everything is populated, so this component is not considered empty
			return false;
		}
	}

	/**
	 * @return the Image Sling Model of this resource, or null if the resource cannot create a valid Image Sling Model.
	 */
	private Image getImage() {
		return image;
	}

}
