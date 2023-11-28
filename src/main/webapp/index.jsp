<%@ page import="java.rmi.Naming" %>
<%@ page import="interfaces.*" %>
<%@ page import="java.util.*" %>
<%
Map<ITeacher,Set<IAppointment>> teacher=null;
    try {
    	ITutoringServer server = (ITutoringServer) Naming.lookup("TutoringPlatform");
    	
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<p>Message from server: <%=teacher%></p>
<h1>Welcome to the Home Page</h1>
<a href="nextPage.jsp">Go to Next Page</a>
<div id="example-table"></div>



<script type="text/javascript" src="dist/js/tabulator.min.js"></script>
<script type="text/javascript" src="dist/js/jquery_wrapper.min.js"></script>
<script type="text/javascript" src="https://unpkg.com/tabulator-tables@4.9.3/dist/js/tabulator.min.js"></script>
<link href="https://unpkg.com/tabulator-tables@4.9.3/dist/css/tabulator.min.css" rel="stylesheet">

<script>

//define data array
var tabledata = [
    {id:1, name:"Oli Bob", progress:12, gender:"male", rating:1, col:"red", dob:"19/02/1984", car:1},
    {id:2, name:"Mary May", progress:1, gender:"female", rating:2, col:"blue", dob:"14/05/1982", car:true},
    {id:3, name:"Christine Lobowski", progress:42, gender:"female", rating:0, col:"green", dob:"22/05/1982", car:"true"},
    {id:4, name:"Brendon Philips", progress:100, gender:"male", rating:1, col:"orange", dob:"01/08/1980"},
    {id:5, name:"Margret Marmajuke", progress:16, gender:"female", rating:5, col:"yellow", dob:"31/01/1999"},
    {id:6, name:"Frank Harbours", progress:38, gender:"male", rating:4, col:"red", dob:"12/05/1966", car:1},
];

//initialize table
var table = new Tabulator("#example-table", {
    data:tabledata, //assign data to table
    autoColumns:true, //create columns from data field names
    layout:"fitColumns",
    pagination: "local",
    paginationSize: 25,
    paginationSizeSelector: [25, 50, 60],
    movableColumns: true,
    rowContextMenu: "rowMenu",
    paginationButtonCount: 30,
    selectable: true,
});


</script>


<script src="js/jquery-3.4.1.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/custom.js"></script>

