package org.zerock.b01.domain;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imgSet")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<BoardImage> imgSet = new HashSet<>();

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addImage(String fileLink){

        BoardImage  image = BoardImage.builder().fileLink(fileLink)
                .ord(imgSet.size())
                .build();
        imgSet.add(image);
    }

    public void clearImgSet() {

        imgSet.clear();
    }

}
