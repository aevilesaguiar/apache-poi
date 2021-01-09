/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestXWPFComment {
    @Test
    void testText() throws IOException {
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("comment.docx")) {
            assertEquals(1, doc.getComments().length);
            XWPFComment comment = doc.getComments()[0];
            assertEquals("Unbekannter Autor", comment.getAuthor());
            assertEquals("0", comment.getId());
            assertEquals("This is the first line\n\nThis is the second line", comment.getText());
        }
    }
}
