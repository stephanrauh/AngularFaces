/* Create an array with the values of all the input boxes in a column */
$.fn.dataTable.ext.order['dom-text-generic'] = function  ( settings, col )
{
  return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    var type=$('input', td).attr('type');
    console.log(type);
    if ("text"==type)
      return $('input', td).val();
    else if ("number"==type) 
      return $('input', td).val() * 1;
    else return 0;
  } );
}


/* Create an array with the values of all the input boxes in a column */
$.fn.dataTable.ext.order['dom-text'] = function  ( settings, col )
{
  return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    console.log($('input', td).attr('type'));
    return $('input', td).val();
  } );
}

/* Create an array with the values of all the input boxes in a column, parsed as numbers */
$.fn.dataTable.ext.order['dom-text-numeric'] = function  ( settings, col )
{
  return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    return $('input', td).val() * 1;
  } );
}

/* Create an array with the values of all the select options in a column */
$.fn.dataTable.ext.order['dom-select'] = function  ( settings, col )
{
  return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    return $('select', td).val();
  } );
}

/* Create an array with the values of all the checkboxes in a column */
$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
{
  return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    return $('input', td).prop('checked') ? '1' : '0';
  } );
}

var dataSet = [
               ['VW',    'Golf',  '2008'],
               ['Honda', 'Civic', '2012'],
               ['BMW',   '320i',  '2014']];
$(document).ready(function() {

  $('#gebrauchtwagenTabellenID').dataTable( {
    responsive: true,
    "columns": [
                { "orderDataType":  "dom-text-generic", type: "string"},
                { "orderDataType":  "dom-text-generic", type: "string"},
                { "orderDataType":  "dom-text-generic", type: "string"}
                ],
                "columnDefs": [
                               {
                                 // The `data` parameter refers to the data for the cell (defined by the
                                 // `data` option, which defaults to the column being worked with, in
                                 // this case `data: 0`.
                                 "render": function ( data, type, row, meta ) {
                                   var id ="row-" + meta.row + "-" + meta.col;
                                   id = 'id="' + id + '"';
                                   var type="text";
                                   if (meta.col==2) type="number";
                                   var cell = 'input ' + id + ' type="'+type+'" value="'+data + '" />';
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

} );