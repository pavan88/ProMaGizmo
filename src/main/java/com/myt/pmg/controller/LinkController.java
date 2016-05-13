package com.myt.pmg.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myt.pmg.common.UtilFunction;
import com.myt.pmg.dto.LUV;
import com.myt.pmg.dto.LinkRecieverDTO;
import com.myt.pmg.dto.LinkStatus;
import com.myt.pmg.dto.LinkVerifierDTO;
import com.myt.pmg.model.Feedback;
import com.myt.pmg.model.Link;
import com.myt.pmg.model.User;
import com.myt.pmg.model.UserLink;
import com.myt.pmg.service.FeedbackService;
import com.myt.pmg.service.LinkService;
import com.myt.pmg.service.UserLinkService;
import com.myt.pmg.service.UserService;

@Controller
public class LinkController {
	static Logger logger = Logger.getLogger(LinkController.class);

	@Autowired
	private LinkService linkService;

	@Autowired
	private UserLinkService userLinkService;

	/*
	 * @Autowired private ProofService proofService;
	 */

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private UserService userService;

	public void setLinkService(LinkService linkService) {
		this.linkService = linkService;
	}

	public void setFeedbackService(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	public void setUserLinkService(UserLinkService userLinkService) {
		this.userLinkService = userLinkService;
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkbroadcaster", method = RequestMethod.GET)
	public String showlinkbroadcaster(Model model, HttpSession session) {
		User user = UtilFunction.getCurrentUser(session);
		if (null == user) {
			logger.debug("InValid User!!!!");
			return "login";
		}

		// LID Already generated send the values LID, AD-URL to SAME PAGE (JSP)
		// to display
		List<Link> linkLists = linkService.getLinksPostedByUser(user.getId());
		// Add null or empty checks
		// How if we remove the line of code 85
		model.addAttribute("linkLists", linkLists);
		model.addAttribute("user", user);

		return "linkbroadcaster";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkbroadcaster", method = RequestMethod.POST)
	public String createNewLink(@RequestParam("generateLID") String generateLID, @RequestParam("urls") String urls,
			HttpSession session, Model model, HttpServletRequest request) {
		User user = UtilFunction.getCurrentUser(session);
		if (null == user) {
			// Send User to same Page showing user is not logged in
			return "login";
		}

		TreeSet<String> uniqueurls = new TreeSet<String>();

		StringTokenizer strtkn = new StringTokenizer(urls, "\r\n");
		while (strtkn.hasMoreTokens()) {
			uniqueurls.add(strtkn.nextToken());
		}

		// generateLID --->true then generate the LID now.
		if (generateLID.equalsIgnoreCase("true")) {

			Iterator<String> itr = uniqueurls.iterator();
			while (itr.hasNext()) {
				String url = itr.next();
				Link link = new Link();
				if (linkService.isurlexists(url)) {
					if (url.contains(user.getDomain())) {
						link.setUrl(url);
						System.out.println("URL doesnot exisits!!!!!!!!!!!!");
						// Submit the link for Broadcast
						linkService.submitLink(link, user);
					}
				}

			}
		}

		if (generateLID.equalsIgnoreCase("false")) {
			// Logic to broadcast the link
			System.out.println("Start Broadcasting");
			System.out.println("*****Selecting the user to send the link**************");
			User selecteduser = userService.getRandomUser();
			System.out.println("*****Selecting the user to send the link**************" + selecteduser.getEmail());
			String randomuserSelected = selecteduser.getId();
			while (user.getId().equalsIgnoreCase(randomuserSelected)) {
				System.out.println("Generating the Random User......");
				selecteduser = userService.getRandomUser();
				randomuserSelected = selecteduser.getId();
			}

			// user.getId -Logged in User , LID created
			// randomselecteduser - chosen to broadcast LID
			if (!user.getId().equalsIgnoreCase(randomuserSelected)) {
				List<Link> links = linkService.findByUserId(user.getId());
				for (Link link : links) {
					if (!link.isBroadcasted()) {
						boolean result = userLinkService.linkPosted(randomuserSelected, link.getId(), user.getId());
						if (result) {
							link.setBroadcasted(true);
							linkService.update(link);
							System.out.println("LInk Broad Casted to User Email::" + selecteduser.getEmail()
									+ ">***************<" + link.getLid());
						}
					}
				}

			} else {
				System.out.println("Broadcaster and reciever are same........");
				return "dashboard";
			}

			return "dashboard";
		}
		return "redirect:/linkbroadcaster";
		// return "dashboard";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping("/deletelink")
	public void deleteLink() {
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkverifierdata", produces = "application/json")
	@ResponseBody
	public List<LinkVerifierDTO> linkVerifierData(HttpSession session, HttpServletResponse response) {

		User user = UtilFunction.getCurrentUser(session);
		String userid = user.getId();
		List<LinkVerifierDTO> linklist = linkService.getLinksForVerfication(userid);
		if (linklist.isEmpty()) {
			logger.error("verification list empty");
		}
		return linklist;
	}

	/*
	 * @PreAuthorize(value = "hasRole('ROLE_USER')")
	 * 
	 * @RequestMapping(value = "/linkrecieverdata", produces =
	 * "application/json")
	 * 
	 * @ResponseBody // @RequestMapping(value="/linkreciever") public
	 * List<LinkRecieverDTO> linkReciever(HttpSession session,
	 * HttpServletResponse response) { // Here Add Logic to get top N Link and N
	 * will be coming from Requet // Parameter for eg N and Pass N to database
	 * so we will not be loading // all Link and thas has not upload proof User
	 * user = UtilFunction.getCurrentUser(session); if (user == null) {
	 * session.invalidate(); } String userid = user.getId();
	 * List<LinkRecieverDTO> linklist =
	 * linkService.getLinksPostedToUser(userid); if (linklist.isEmpty()) {
	 * logger.error("list is empty"); } return linklist; }
	 */

	@RequestMapping(value = "/linkstatus", method = RequestMethod.GET)
	public String linkStatus(HttpSession session, HttpServletResponse response) {
		User user = UtilFunction.getCurrentUser(session);
		String userid = user.getId();
		LinkStatus ls = new LinkStatus();
		ls.setLinksClicked(userLinkService.countLinksPostedToUserAndClicked(userid)
				/ userLinkService.countLinksPostedToUser(userid));
		ls.setLinksVerified(userLinkService.countLinksPostedToUserAndVerified(userid)
				/ userLinkService.countLinksPostedToUserAndClicked(userid));
		ls.setLinksVerificationPending((userLinkService.countLinksPostedToUserAndClicked(userid)
				- userLinkService.countLinksPostedToUserAndVerified(userid))
				/ userLinkService.countLinksPostedToUserAndClicked(userid));
		ls.setLinksServed(userLinkService.countLinksServedByUser(userid));
		ls.setLinksApproved(userLinkService.countLinksServedByUserAndApproved(userid)
				/ userLinkService.countLinksServedByUser(userid));
		ls.setLinksDisapproved(userLinkService.countLinksServedByUserAndDisapproved(userid)
				/ userLinkService.countLinksServedByUser(userid));
		System.out.println(ls);
		// return ls;
		return "linksnclicks-status";

	}

	@RequestMapping(value = "/gettoplinks", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<LUV> getTopLinks(HttpSession session) {
		List<LUV> luvList = linkService.findTopLinks(5);
		return luvList;
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkreciever", method = RequestMethod.GET)
	public String linkRecieverPage(HttpSession session, Model model, HttpServletRequest request) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null) {
			session.invalidate();
			return "login";
		}

		List<UserLink> userlinks = userLinkService.findByUserid(user.getId());

		List<LinkRecieverDTO> linkrecvedlist = new ArrayList<LinkRecieverDTO>();

		if (userlinks == null) {
			return "linkreciever";
			// throw new Exception("No links recieved");
		}
		for (UserLink userlink : userlinks) {
			String linkId = userlink.getLinkId();
			Link link = linkService.findById(linkId);
			User broadcastedUser = userService.findById(link.getUserId());

			LinkRecieverDTO linkrecvDTO = new LinkRecieverDTO();
			linkrecvDTO.setAdurl(link.getUrl());
			linkrecvDTO.setLid(Integer.valueOf(link.getLid()));
			linkrecvDTO.setLinkrecvdDate(userlink.getLinkrecvDate());
			linkrecvDTO.setUsername(broadcastedUser.getFirstname());

			linkrecvedlist.add(linkrecvDTO);

		}

		model.addAttribute("linkrecvedlist", linkrecvedlist);

		return "linkreciever";

	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkreciever", method = RequestMethod.POST)
	public String linkVerification(HttpSession session, @RequestParam("videourl") String videourl) {
		System.out.println("Here in POST Method + link recver");
		User user = UtilFunction.getCurrentUser(session);
		if (user == null) {
			session.invalidate();
			return "login";
		}
		List<UserLink> userlinks = userLinkService.findByUserid(user.getId());

		for (UserLink userlink : userlinks) {
			userlink.setClicked(true);
			// LOGIC to validate the URL.TO-DO
			userlink.setVideourl(videourl);
			userLinkService.update(userlink);
		}
		return "dashboard";

	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/proofresult", method = RequestMethod.GET)
	public String showLinkVerifierPage(HttpServletRequest request, HttpSession session, Model model) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null) {
			session.invalidate();
			return "logout";
		}

		List<UserLink> userlinks = userLinkService.findByBroadcasterId(user.getId());
		List<Link> links = new ArrayList<Link>();
		List<LinkVerifierDTO> linkverifiers = new ArrayList<LinkVerifierDTO>();
		LinkVerifierDTO linkVerifierDTO = new LinkVerifierDTO();

		if (userlinks == null) {
			return "linkverifier";
		}

		for (UserLink userlink : userlinks) {
			String linkId = userlink.getLinkId();

			Link link = linkService.findById(linkId);

			linkVerifierDTO.setDomain(user.getDomain());
			linkVerifierDTO.setLinkId(link.getLid());
			// LOGIC to replace the watch with embed in the URL
			String youtubestr = userlink.getVideourl();
			StringBuilder builder = new StringBuilder(youtubestr);
			builder.replace(25, 33, "embed/");

			linkVerifierDTO.setProofFileName(youtubestr);
			linkVerifierDTO.setSubmissionDate(new Date());
			linkVerifierDTO.setUrl(link.getUrl());
			// linkVerifierDTO.setUserName(userlink);

			// links.add(link);
			// model.addAttribute("link", link);
			break;

		}
		model.addAttribute("linkVerifierDTO", linkVerifierDTO);

		/*
		 * User posteduser = userService.findById(link.getUserId());
		 * model.addAttribute("posteduser", posteduser);
		 */
		return "linkverifier";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/proofresult", method = RequestMethod.POST)
	@ResponseBody
	public String showLinkVerifier(HttpServletRequest request, HttpSession session) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null) {
			session.invalidate();
		}
		int i = 0;
		while (request.getParameter("linkId" + i) != null) {
			UserLink userlink = userLinkService.findByLinkId(request.getParameter("linkId" + i));
			if (userlink != null) {
				int rating = Integer.parseInt(request.getParameter("rating" + i));
				if (rating == -99) {
					// proofService.delete(userlink.getProofId());
					userlink.setVerified(false);
				} else {
					userlink.setVerified(true);
					Feedback fb = new Feedback();
					fb.setUserId(userlink.getUserId());
					fb.setRating(rating);
					feedbackService.create(fb);
				}
				userLinkService.update(userlink);
				i++;
			}
		}
		return "dashboard";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping("/mylinks")
	@ResponseBody
	public List<LUV> getMyLinks(HttpSession session) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null)
			session.invalidate();
		List<Link> linkList = linkService.getLinksPostedByUser(user.getId());
		List<LUV> luvList = new ArrayList<LUV>();
		for (Link link : linkList) {
			long days = (new Date().getTime() - (link.getCreationTime()).getTime()) / (24 * 3600 * 1000l);
			luvList.add(new LUV(link.getCreationTime(), link.getId(), null, link.getUrl(),
					userLinkService.countClicksOnLink(link.getId()), days, link.isActive()));
		}
		return luvList;
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping("/luv")
	public String getLinkUniqueness(HttpSession session) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null)
			session.invalidate();
		/*
		 * List<Link> linkList = linkService.getLinksPostedByUser(user.getId());
		 * List<LUV> luvList = new ArrayList<LUV>(); for (Link link : linkList)
		 * { long days = (new Date().getTime() - (link.getCreationTime())
		 * .getTime()) / (24 * 3600 * 1000l); luvList.add(new
		 * LUV(link.getCreationTime(), link.getId(), null, link.getUrl(),
		 * userLinkService.countClicksOnLink(link .getId()), days,
		 * link.isActive())); }
		 */
		return "luv";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping("/complaints")
	public String complaints(HttpSession session) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null)
			session.invalidate();

		return "complaints";
	}

	@RequestMapping(value = "/editmylinks", method = RequestMethod.POST)
	@ResponseBody
	public String showEditFaq(HttpServletRequest request) {
		User user = UtilFunction.getCurrentUser(request.getSession());
		if (user == null) {
			request.getSession().invalidate();
		}
		int i = 0;
		while (request.getParameter("linkid" + i) != null) {
			Link link = linkService.findById(request.getParameter("linkid" + i));
			if (link != null) {
				if (request.getParameter("checklink" + i).equalsIgnoreCase("true")) {
					link.setActive(true);
				} else
					link.setActive(false);
				linkService.update(link);
			}
			i++;
		}
		return "success";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping("/contributors")
	public String contributors(HttpSession session, Model model) {
		User user = UtilFunction.getCurrentUser(session);
		if (user == null)
			session.invalidate();
		model.addAttribute("user", user);
		return "contributors";
	}

}
