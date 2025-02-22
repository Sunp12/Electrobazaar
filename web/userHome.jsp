<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.electrobazaar.dao.impl.*,in.electrobazaar.pojo.*,in.electrobazaar.utility.*,in.electrobazaar.dao.*,java.util.*, javax.servlet.ServletOutputStream.*,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title><%=AppInfo.appName%> Application</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

<link rel="stylesheet" href="mycss.css">
</head>
<body style="background-color: #E6F9E6;">

	<jsp:include page="header.jsp" />
                <% if(request.getAttribute("message")!=null){
                 %>
	<div id="message" class="text-center h3 text-primary m-3"><%=request.getAttribute("message")%></div>
           <%}%>
	<!-- <script>document.getElementById('mycart').innerHTML='<i data-count="20" class="fa fa-shopping-cart fa-3x icon-white badge" style="background-color:#333;margin:0px;padding:0px; margin-top:5px;"></i>'</script>
 -->
	<!-- Start of Product Items List -->
	<div class="container">
		<div class="row text-center">

			<%
//                            Map<String, Integer>mapList=(HashMap)request.getAttribute("map");
//                            String userName=(String)request.getAttribute("userName");
//                            List<ProductPojo>prodList=(ArrayList<ProductPojo>)request.getAttribute("products");
                               Map<String, Integer>mapList=(HashMap)session.getAttribute("map");
                               String userName=(String)session.getAttribute("userName");
                               List<ProductPojo>prodList=(ArrayList<ProductPojo>)session.getAttribute("products");
                                for(ProductPojo prod:prodList){
                                int cartQty=mapList.get(prod.getProdId());
                                  
			%>
			<div class="col-sm-4">
				<div class="thumbnail mt-3 mb-3">
					<img src="./ShowImageServlet?pid=<%=prod.getProdId()%>" alt="Product"
						style="height: 150px; max-width: 180px" class="mt-3">
					<p class="productname"><%=prod.getProdName()%>
					</p>
					<%
					 String description=prod.getProdInfo();
                                         description=description.substring(0,Math.min(100,description.length()));
                          
					%>
					<p class="productinfo"><%=description%>..
					</p>
					<p class="price">
						Rs
						<%=prod.getProdPrice()%>
					</p>
					<form method="post">
						<%
						if(cartQty==0){ 
						%>
						<button type="submit"
							formaction="./AddToCartServlet?uid=<%=userName%>&pid=<%=prod.getProdId()%>&pqty=1"
							class="btn btn-warning">Add to Cart</button>
						&nbsp;&nbsp;&nbsp;
						<button type="submit"
                                                        formaction="./AddToCartServlet?uid=<%=userName%>&pid=<%=prod.getProdId()%>&pqty=1&action=buy"
							formaction="cartDetails.jsp"
							class="btn btn-primary">Buy Now</button>
						<%
						}else{
						%>
						<button type="submit"
							formaction="./AddToCartServlet?uid=<%=userName%>&pid=<%=prod.getProdId()%>&pqty=0"
							class="btn btn-danger">Remove From Cart</button>
						&nbsp;&nbsp;&nbsp;
						<button type="submit" formaction="./CartDetailsServlet"
							class="btn btn-success">Checkout</button>
						<%
						  }
						%>
					</form>
					<br />
				</div>
			</div>

			<%
			  }  
			%>

		</div>
	</div>
	<!-- ENd of Product Items List -->


	<%@ include file="footer.jsp"%>

</body>
</html>
