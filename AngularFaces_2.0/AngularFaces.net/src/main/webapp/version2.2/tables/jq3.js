      var dataSet = [
                     ['VW',    'Golf',  '2008'],
                     ['Honda', 'Civic', '2012'],
                     ['BMW',   '320i',  '2014']];
      $(document).ready(function() {
    	  
    	   $('#gebrauchtwagenTabellenID').dataTable( {
    		   responsive: true,
    	        "columnDefs": [
    	            {
    	                // The `data` parameter refers to the data for the cell (defined by the
    	                // `data` option, which defaults to the column being worked with, in
    	                // this case `data: 0`.
    	                "render": function ( data, type, row, meta ) {
    	                	var id ="row-" + meta.row + "-" + meta.col;
    	                	id = 'id="' + id + '"';
    	                    var cell = 'input ' + id + ' type="text" value="'+data + '" />';
    	                    return "<" + cell;
    	                },
    	                "targets": [0, 1, 2]
    	            }
    	        ]
    	    } );
    	                
    	    var table = $('#gebrauchtwagenTabellenID').DataTable();
    	    
    	    dataSet.forEach(function(row) {
        		  var col1=row[0];
          		  var col2=row[1];
          		  var col3=row[2];
      		  table.row.add( [col1,col2,col3]);
      		  table.draw();
      	  });

/*    	                
    	  var table = $('#gebrauchtwagenTabellenID').DataTable();
    	  
    	  dataSet.forEach(function(row) {
    		  var col1='<td data-order="'+row[0]+'"><input type="text" id="row-1-0" name="row-1-0" value="' + row[0]+'"/></td>';
    		  var col2='<td data-order="'+row[0]+'"><input type="text" id="row-1-1" name="row-1-1" value="' + row[1]+'"/></td>';
    		  var col3='<td data-order="'+row[0]+'"><input type="text" id="row-1-2" name="row-1-2" value="' + row[2]+'"/></td>';
    		  table.row.add( [col1,col2,col3]);
    	  });
    	  table.draw();
    	  table.rows()[0];
    	  */
      } );