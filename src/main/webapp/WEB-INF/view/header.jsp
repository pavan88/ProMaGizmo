<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul>
	<li class="waves-effect waves-light"><a><c:out
				value="${user.level}" /><i class="fa fa-star yellow-text"></i></a></li>
	<li class="waves-effect waves-light"><a class="tooltipped"
		data-position="buttom" data-delay="50"
		data-tooltip="Notification / Messages"><i class="fa fa-bell"></i><sup
			class="badge red white-text">0</sup></a></li>
	<li class="user"><a id="step4" class="dropdown-button" href="#!"
		data-activates="user-dropdown"> <img
			src="${pageContext.request.contextPath}/static/assets/core/pmg/images/user2.jpg"
			alt="John Doe" class="circle"> <c:out value="${user.firstname}" /><i
			class="mdi-navigation-expand-more right"></i>
	</a>
		<ul id="user-dropdown" class="dropdown-content">
			<li id="step3"><a href="profile"><i class="fa fa-user"></i>
					Profile</a></li>
			<li><a href="notify"><i class="fa fa-envelope"></i>
					Notification <span class="badge new">2</span></a></li>
			<li><a href="account-setup.html"><i class="fa fa-cogs"></i>
					Settings</a></li>
			<li><a href="lock.html"><i class="mdi mdi-action-lock"></i>
					Lock Account</a></li>
			<li class="divider"></li>
			<li><a href="logout"><i class="mdi-action-settings-power"></i>
					Logout</a></li>
		</ul></li>
</ul>