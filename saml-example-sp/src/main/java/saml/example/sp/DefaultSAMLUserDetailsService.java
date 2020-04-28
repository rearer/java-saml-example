package saml.example.sp;

import static java.util.stream.Collectors.toList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.opensaml.saml2.core.NameID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import saml.example.core.SAMLAttribute;
import saml.example.core.SAMLBuilder;
import saml.example.core.SAMLPrincipal;

public class DefaultSAMLUserDetailsService implements SAMLUserDetailsService {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public Principal loadUserBySAML(SAMLCredential credential) {
		
		LOG.debug("loadUserBySAML {}", credential);
		LOG.debug("loadUserBySAML {}", credential.getAttributes());
		LOG.debug("loadUserBySAML {}", credential.getNameID().getValue());
		LOG.debug("loadUserBySAML {}", credential.getNameID().getFormat());

	    List<SAMLAttribute> attributes = credential.getAttributes().stream().map(attribute ->
	      new SAMLAttribute(
	        attribute.getName(),
	        attribute.getAttributeValues().stream().map(SAMLBuilder::getStringValueFromXMLObject)
	          .filter(Optional::isPresent).map(Optional::get).collect(toList()))).collect(toList());

		NameID nameID = credential.getNameID();

		return new SAMLPrincipal(nameID.getValue(), nameID.getFormat(), attributes);
	}

}
