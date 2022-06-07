package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of="fileLink")
public class BoardImage {

    private String fileLink;

    private int ord;
}
