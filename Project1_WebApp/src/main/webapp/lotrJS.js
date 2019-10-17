let user;
let server = "http://ec2-18-221-114-64.us-east-2.compute.amazonaws.com:8080/Project1_WebApp";

    function logout(){
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
                user = null;

                let body = document.getElementById("body");
                let LPage = document.getElementById("LoginPage");
                let UPage = document.getElementById("UserPage");
                let MPage = document.getElementById("ManagerPage");
                let MTable = document.getElementById('ManagerTable'); 
                let UTable = document.getElementById('UserTable'); 

                //change visiblity
                body.style.backgroundImage = "url('map.jpeg')";
                LPage.style.display = "block";
                MPage.style.display = "none";
                UPage.style.display = "none";

                //unpopulate tables
                MTable.innerHTML="";
                UTable.innerHTML="";
            }
        }
        xhttp.open("post", `${server}/logout.do`, true);
        xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhttp.send(`username=${uname}&password=${pass}`);
    }





//LoginPage JS
    function login(){

        let uname = document.getElementById("uname").value;
        let pass = document.getElementById("pass").value;
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
                if(this.responseText==-1){
                    document.getElementById("ErrorBox").innerHTML="Login Failed";
                }else{
                    user = JSON.parse(this.responseText);

                    let body = document.getElementById("body");
                    let LPage = document.getElementById("LoginPage");
                    let UPage = document.getElementById("UserPage");
                    let MPage = document.getElementById("ManagerPage");

                    if(user.isManager===1){
                        LPage.style.display = "none";
                        MPage.style.display = "block";
                        body.style.backgroundImage = "";
                        populateManagerTable();
                    }else{
                        LPage.style.display = "none";
                        UPage.style.display = "block";
                        body.style.backgroundImage = "";
                        populateUserTable();
                    }
                }
            }
        }
        xhttp.open("post", `${server}/login.do`, true);
        xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhttp.send(`username=${uname}&password=${pass}`);
    }

    function makeCoffee(){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
              
            }
            if(this.readyState === 4 && this.status === 418){
                document.getElementsByTagName("html")[0].innerHTML=this.responseText;
            }
        }
        xhttp.open("post", `${server}/coffee.do`, true);
        xhttp.send();
    }

//Manager Page
        let approve = function(id){
            let reimbursement = getRowData(id);

            let xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = reviewCallback;
            xhttp.open("post", `${server}/approve.do`, true);
            xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            console.log(`Approve sent - ${reimbursement}`);
            xhttp.send(`reimbursement=${reimbursement}`);
        }

        let reject = function(id){
            let reimbursement = getRowData(id);

            let xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = reviewCallback;
            xhttp.open("post", `${server}/reject.do`, true);
            xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            xhttp.send(`reimbursement=${reimbursement}`);
        }

        let reviewCallback = function(){
            if(this.readyState === 4 && this.status === 200){
                getAndUpdateById(id);
            }
        }

        function getRowData(id){
            let outJson;
            let MTable = document.getElementById('ManagerTable'); 
            let rows = Array.prototype.slice.call(MTable.getElementsByTagName('tr'));
            let breakException = {};
            try{
                rows.forEach(row => {
                    if(row.children[0].innerHTML==id){

                        let outObject = {};

                        outObject.r_id=id;
                        outObject.submiter=row.children[1].innerHTML;
                        outObject.desc=row.children[2].innerHTML;
                        outObject.price=row.children[3].innerHTML;
                        // outObject.timestamp=row.children[4].innerHTML;
                        outObject.timestamp=0;
                        switch(row.children[5].innerHTML){
                            case 'Approved': outObject.state=1; break;
                            case 'Rejected': outObject.state=2; break;
                            case 'Pending': outObject.state=0; break;
                        }                    
                        outObject.approver=row.children[6].innerHTML;
                        if(row.children[7].children[2]){
                            outObject.comment=row.children[7].children[2].value;
                        } else{
                            outObject.comment=row.children[7].innerHTML;
                        }
                        outJson = JSON.stringify(outObject);
                        throw breakException; //throw exception to break from forEach
                    }
                });
            }catch(e){}
            return outJson;
        }

        function addManagerRow(dataJSON){
            let MTable = document.getElementById('ManagerTable'); 
            let data = JSON.parse(dataJSON);

            let string = `<tr>`;
                string+= `<td>${data.r_id}</td>`;
                string+= `<td>${data.submiter}</td>`;
                string+= `<td>${data.desc}</td>`;
                string+= `<td>${data.price}</td>`;
                let date = new Date(data.timestamp);
                    let minutes = date.getMinutes();
                    if(date.getMinutes()<10){
                        minutes='0'+date.getMinutes();
                    }
                string+= `<td>${date.getMonth()}/${date.getDay()}/${date.getFullYear()} - ${date.getHours()}:${minutes}</td>`;
                if(data.state===1){
                    string+= `<td>Approved</td>`;
                }else if(data.state===2){
                    string+= `<td>Rejected</td>`;
                }else{
                    string+= `<td>Pending</td>`;
                }
                string+= `<td>${data.approver}</td>`;
                if(data.state===0){
                    string+= `<td>
                                <button onclick='approve(${data.r_id})'>A</button>
                                <button onclick='reject(${data.r_id})'>R</button>
                                <input type='text'>
                             </td>`;
                }else{
                    string+= `<td>${data.comment}</td>`;
                }
                string+= `<td style="display: none;">${data.timestamp}</td>`;
                string+= `</tr>`;
            MTable.innerHTML+=string;
        }

        function updateManagerRow(dataJSON){
            let breakException = {};
            let MTable = document.getElementById('ManagerTable'); 
            let data = JSON.parse(dataJSON);
            console.log("Updating Manager Row")
            console.log("data"+data);
            let rows = Array.prototype.slice.call(MTable.getElementsByTagName('tr'));
            try{
                rows.forEach(row => {
                    console.log(`Row ${row.children[0].innerHTML} - ${data.r_id}`)
                    if(row.children[0].innerHTML==data.r_id){

                        console.log("State - "+data.state);
                        if(data.state===1){
                            row.children[5].innerHTML="Approved";
                        }else if(data.state===2){
                            row.children[5].innerHTML="Rejected";
                        }
                                                
                        console.log("Approver - "+data.approver);
                        row.children[6].innerHTML=data.approver;

                        console.log("Comment - "+data.comment);
                        row.children[7].innerHTML=data.comment;

                        throw breakException; //throw exception to break from forEach
                    }
                });
            }catch(e){}
        }

        let getAndUpdateById = function(id){
            let xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function(){
                if(this.readyState === 4 && this.status === 200){
                    console.log(`GetAndUpdateByID recieved - ${this.responseText}`);
                    updateManagerRow(this.responseText);
                }
            }
            xhttp.open("post", `${server}/getReimbursementById.do`, true);
            xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            console.log(`GetAndUpdateByID sent 'r_id=${id}'`);
            xhttp.send(`r_id=${id}`);
        }

        let populateManagerTable = function(){
            let xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function(){
                if(this.readyState === 4 && this.status === 200){
                    let items = JSON.parse(this.responseText);
                    items.forEach(element => {
                        addManagerRow(JSON.stringify(element));
                    });                  
                }
            }
            xhttp.open("post", `${server}/getAllReimbursements.do`, true);
            xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            xhttp.send();
        }

        //  https://www.w3schools.com/howto/howto_js_sort_table.asp
        function sortManagerTable(n) {
            let table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("ManagerTable");
            switching = true;
            // Set the sorting direction to ascending:
            dir = "asc";
            /* Make a loop that will continue until
            no switching has been done: */
            while (switching) {
                // Start by saying: no switching is done:
                switching = false;
                rows = table.rows;
                // Loop through all table rows
                for (i = 0; i < (rows.length - 1); i++) {
                    // Start by saying there should be no switching:
                    shouldSwitch = false;
                    /* Get the two elements you want to compare,
                    one from current row and one from the next: */
                    x = rows[i].getElementsByTagName("TD")[n];
                    y = rows[i + 1].getElementsByTagName("TD")[n];
                    /* Check if the two rows should switch place,
                    based on the direction, asc or desc: */
                    if(n===3 || n===8){ //sort by Price or Date
                        if (dir == "asc") {
                            if (Number(x.innerHTML) > Number(y.innerHTML)) {
                                // If so, mark as a switch and break the loop:
                                shouldSwitch = true;
                                break;
                            }
                        } else if (dir == "desc") {
                            if (Number(x.innerHTML) < Number(y.innerHTML)) {
                                // If so, mark as a switch and break the loop:
                                shouldSwitch = true;
                                break;
                            }
                        }
                    }else{ //sort by string
                        if (dir == "asc") {
                            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                                // If so, mark as a switch and break the loop:
                                shouldSwitch = true;
                                break;
                            }
                        } else if (dir == "desc") {
                            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                                // If so, mark as a switch and break the loop:
                                shouldSwitch = true;
                                break;
                            }
                        }
                 }
                }
                if (shouldSwitch) {
                    /* If a switch has been marked, make the switch
                    and mark that a switch has been done: */
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    // Each time a switch is done, increase this count by 1:
                    switchcount ++;
                } else {
                    /* If no switching has been done AND the direction is "asc",
                    set the direction to "desc" and run the while loop again. */
                    if (switchcount == 0 && dir == "asc") {
                    dir = "desc";
                    switching = true;
                    }
                }
            }
        }

//Users Page
    function submit(){
        let amount = document.getElementById("inputAmount").value;
        let desc = document.getElementById("inputDesc").value;

        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
                if(this.responseText=="fail"){
                    document.getElementById("submitError").innerHTML="Submit Failed";
                }else{
                    addUserRow(this.responseText);
                }      
            }
        }
        xhttp.open("post", `${server}/openReimbursement.do`, true);
        xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhttp.send(`description=${desc}&amount=${amount}`);
    }

    function addUserRow(dataJSON){
        let UTable = document.getElementById('UserTable'); 
        let data = JSON.parse(dataJSON);

        let string = `<tr>`;
            string+= `<td>${data.r_id}</td>`;
            string+= `<td>${data.desc}</td>`;
            string+= `<td>${data.price}</td>`;
            let date = new Date(data.timestamp);
                let minutes = date.getMinutes();
                if(date.getMinutes()<10){
                    minutes='0'+date.getMinutes();
                }
            string+= `<td>${date.getMonth()}/${date.getDay()}/${date.getFullYear()} - ${date.getHours()}:${minutes}</td>`;
            if(data.state===1){
                string+= `<td>Approved</td>`;
            }else if(data.state===2){
                string+= `<td>Rejected</td>`;
            }else{
                string+= `<td>Pending</td>`;
            }
            string+= `<td>${data.approver}</td>`;
            string+= `<td>${data.approver}</td>`;
            if(data.comment){
                string+= `<td>${data.comment}</td>`;
            }else{
                string+= `<td></td>`;
            }
            string+= `</tr>`;
        UTable.innerHTML+=string;
    }

    let populateUserTable = function(){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
                let items = JSON.parse(this.responseText);
                items.forEach(element => {
                    addUserRow(JSON.stringify(element));
                });                  
            }
        }
        xhttp.open("post", `${server}/getAllReimbursementsByUser.do`, true);
        xhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhttp.send();
    }