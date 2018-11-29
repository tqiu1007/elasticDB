Question 1: 
public static Vector getBestSellers(String subject) {
        Vector vec = new Vector();  // Vector of Books
        try {
            // Prepare SQL
            Connection con = getConnection();
            //The following is the original, unoptimized best sellers query.
            PreparedStatement statement = con.prepareStatement
                ("SELECT i_id, i_title, a_fname, a_lname " +"FROM item, author, 
                order_line " +"WHERE item.i_id = order_line.ol_i_id " +"AND item.i_a_id = author.a_id " 
                +"AND order_line.ol_o_id > (SELECT MAX(o_id)-3333 FROM orders) " +"AND item.i_subject = ? " 
                +"GROUP BY i_id, i_title, a_fname, a_lname " +"ORDER BY SUM(ol_qty) DESC " +"limit 10");  // change the limit value 50 to 10
            //This is Mikko's optimized version, which depends on the fact that
            //A table named "bestseller" has been created.
            /*PreparedStatement statement = con.prepareStatement
                ("SELECT bestseller.i_id, i_title, a_fname, a_lname, ol_qty " + 
                 "FROM item, bestseller, author WHERE item.i_subject = ?" +
                 " AND item.i_id = bestseller.i_id AND item.i_a_id = author.a_id " + 
                 " ORDER BY ol_qty DESC FETCH FIRST 50 ROWS ONLY");*/

            // Set parameter
            statement.setString(1, subject);
            ResultSet rs = statement.executeQuery();

            // Results
            while(rs.next()) {
                vec.addElement(new ShortBook(rs));
            }
            rs.close();
            statement.close();
            con.commit();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        return vec;
    }

Question 2: UserSession里面，在得到queryclass的String之后，如何得到一个query instance的?
通过query instance factory 获得query instance。比如QueryInstanceFactory.getInstance(queryString)

Question 3: 1)根据在tpcw.properties里面的设置，支持多种load balancer。default是round robin，其他选项有，random，和least-latency（2）random的意思是在选择read server的时候，random选择read的server。（3）（加分）least-latency的意思是在选择read server的时候，根据其以前的read的平均latency选择最短的那个。(有一些特殊情况需要考虑)

